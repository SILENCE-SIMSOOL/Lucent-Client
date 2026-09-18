package silence.simsool.lucentclient.mods.impl.performance.culling;

import java.util.Arrays;
import java.util.BitSet;

import silence.simsool.lucentclient.mods.impl.performance.culling.cache.ArrayOcclusionCache;
import silence.simsool.lucentclient.mods.impl.performance.culling.cache.OcclusionCache;
import silence.simsool.lucentclient.mods.impl.performance.culling.util.MathUtilities;
import silence.simsool.lucentclient.mods.impl.performance.culling.util.Vec3d;

public class OcclusionCullingInstance {

	private static final int ON_MIN_X = 0x01;
	private static final int ON_MAX_X = 0x02;
	private static final int ON_MIN_Y = 0x04;
	private static final int ON_MAX_Y = 0x08;
	private static final int ON_MIN_Z = 0x10;
	private static final int ON_MAX_Z = 0x20;

	private final int reach;
	private final double aabbExpansion;
	private final DataProvider provider;
	private final OcclusionCache cache;

	private final BitSet skipList = new BitSet();
	private final Vec3d[] targetPoints = new Vec3d[15];
	private final Vec3d targetPos = new Vec3d(0, 0, 0);
	private final int[] cameraPos = new int[3];
	private final boolean[] dotselectors = new boolean[14];
	private boolean allowRayChecks = false;
	private final int[] lastHitBlock = new int[3];
	private boolean allowWallClipping = false;

	public OcclusionCullingInstance(int maxDistance, DataProvider provider) {
		this(maxDistance, provider, new ArrayOcclusionCache(maxDistance), 0.5);
	}

	public OcclusionCullingInstance(int maxDistance, DataProvider provider, OcclusionCache cache, double aabbExpansion) {
		this.reach = maxDistance;
		this.provider = provider;
		this.cache = cache;
		this.aabbExpansion = aabbExpansion;
		for (int i = 0; i < this.targetPoints.length; i++) {
			this.targetPoints[i] = new Vec3d(0, 0, 0);
		}
	}

	public boolean isAABBVisible(Vec3d aabbMin, Vec3d aabbMax, Vec3d viewerPosition) {
		try {
			int maxX = MathUtilities.floor(aabbMax.x + this.aabbExpansion);
			int maxY = MathUtilities.floor(aabbMax.y + this.aabbExpansion);
			int maxZ = MathUtilities.floor(aabbMax.z + this.aabbExpansion);
			int minX = MathUtilities.floor(aabbMin.x - this.aabbExpansion);
			int minY = MathUtilities.floor(aabbMin.y - this.aabbExpansion);
			int minZ = MathUtilities.floor(aabbMin.z - this.aabbExpansion);

			this.cameraPos[0] = MathUtilities.floor(viewerPosition.x);
			this.cameraPos[1] = MathUtilities.floor(viewerPosition.y);
			this.cameraPos[2] = MathUtilities.floor(viewerPosition.z);

			Relative relX = Relative.from(minX, maxX, this.cameraPos[0]);
			Relative relY = Relative.from(minY, maxY, this.cameraPos[1]);
			Relative relZ = Relative.from(minZ, maxZ, this.cameraPos[2]);

			if (relX == Relative.INSIDE && relY == Relative.INSIDE && relZ == Relative.INSIDE) {
				return true;
			}

			this.skipList.clear();

			int id = 0;
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						int cachedValue = getCacheValue(x, y, z);
						if (cachedValue == 1) {
							return true;
						}
						if (cachedValue != 0) {
							this.skipList.set(id);
						}
						id++;
					}
				}
			}

			this.allowRayChecks = false;

			id = 0;
			for (int x = minX; x <= maxX; x++) {
				byte visibleOnFaceX = 0;
				byte faceEdgeDataX = 0;
				faceEdgeDataX |= (x == minX) ? ON_MIN_X : 0;
				faceEdgeDataX |= (x == maxX) ? ON_MAX_X : 0;
				visibleOnFaceX |= (x == minX && relX == Relative.POSITIVE) ? ON_MIN_X : 0;
				visibleOnFaceX |= (x == maxX && relX == Relative.NEGATIVE) ? ON_MAX_X : 0;

				for (int y = minY; y <= maxY; y++) {
					byte faceEdgeDataY = faceEdgeDataX;
					byte visibleOnFaceY = visibleOnFaceX;
					faceEdgeDataY |= (y == minY) ? ON_MIN_Y : 0;
					faceEdgeDataY |= (y == maxY) ? ON_MAX_Y : 0;
					visibleOnFaceY |= (y == minY && relY == Relative.POSITIVE) ? ON_MIN_Y : 0;
					visibleOnFaceY |= (y == maxY && relY == Relative.NEGATIVE) ? ON_MAX_Y : 0;

					for (int z = minZ; z <= maxZ; z++) {
						byte faceEdgeData = faceEdgeDataY;
						byte visibleOnFace = visibleOnFaceY;
						faceEdgeData |= (z == minZ) ? ON_MIN_Z : 0;
						faceEdgeData |= (z == maxZ) ? ON_MAX_Z : 0;
						visibleOnFace |= (z == minZ && relZ == Relative.POSITIVE) ? ON_MIN_Z : 0;
						visibleOnFace |= (z == maxZ && relZ == Relative.NEGATIVE) ? ON_MAX_Z : 0;

						if (this.skipList.get(id)) {
							id++;
							continue;
						}

						if (visibleOnFace != 0) {
							this.targetPos.set(x, y, z);
							if (isVoxelVisible(viewerPosition, this.targetPos, faceEdgeData, visibleOnFace)) {
								return true;
							}
						}
						id++;
					}
				}
			}

			return false;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return true;
	}

	private boolean isVoxelVisible(Vec3d viewerPosition, Vec3d position, byte faceData, byte visibleOnFace) {
		int targetSize = 0;
		Arrays.fill(this.dotselectors, false);
		if ((visibleOnFace & ON_MIN_X) == ON_MIN_X) {
			this.dotselectors[0] = true;
			if ((faceData & ~ON_MIN_X) != 0) {
				this.dotselectors[1] = true;
				this.dotselectors[4] = true;
				this.dotselectors[5] = true;
			}
			this.dotselectors[8] = true;
		}
		if ((visibleOnFace & ON_MIN_Y) == ON_MIN_Y) {
			this.dotselectors[0] = true;
			if ((faceData & ~ON_MIN_Y) != 0) {
				this.dotselectors[3] = true;
				this.dotselectors[4] = true;
				this.dotselectors[7] = true;
			}
			this.dotselectors[9] = true;
		}
		if ((visibleOnFace & ON_MIN_Z) == ON_MIN_Z) {
			this.dotselectors[0] = true;
			if ((faceData & ~ON_MIN_Z) != 0) {
				this.dotselectors[1] = true;
				this.dotselectors[4] = true;
				this.dotselectors[5] = true;
			}
			this.dotselectors[10] = true;
		}
		if ((visibleOnFace & ON_MAX_X) == ON_MAX_X) {
			this.dotselectors[4] = true;
			if ((faceData & ~ON_MAX_X) != 0) {
				this.dotselectors[5] = true;
				this.dotselectors[6] = true;
				this.dotselectors[7] = true;
			}
			this.dotselectors[11] = true;
		}
		if ((visibleOnFace & ON_MAX_Y) == ON_MAX_Y) {
			this.dotselectors[1] = true;
			if ((faceData & ~ON_MAX_Y) != 0) {
				this.dotselectors[2] = true;
				this.dotselectors[5] = true;
				this.dotselectors[6] = true;
			}
			this.dotselectors[12] = true;
		}
		if ((visibleOnFace & ON_MAX_Z) == ON_MAX_Z) {
			this.dotselectors[2] = true;
			if ((faceData & ~ON_MAX_Z) != 0) {
				this.dotselectors[3] = true;
				this.dotselectors[6] = true;
				this.dotselectors[7] = true;
			}
			this.dotselectors[13] = true;
		}

		if (this.dotselectors[0]) this.targetPoints[targetSize++].setAdd(position, 0.05, 0.05, 0.05);
		if (this.dotselectors[1]) this.targetPoints[targetSize++].setAdd(position, 0.05, 0.95, 0.05);
		if (this.dotselectors[2]) this.targetPoints[targetSize++].setAdd(position, 0.05, 0.95, 0.95);
		if (this.dotselectors[3]) this.targetPoints[targetSize++].setAdd(position, 0.05, 0.05, 0.95);
		if (this.dotselectors[4]) this.targetPoints[targetSize++].setAdd(position, 0.95, 0.05, 0.05);
		if (this.dotselectors[5]) this.targetPoints[targetSize++].setAdd(position, 0.95, 0.95, 0.05);
		if (this.dotselectors[6]) this.targetPoints[targetSize++].setAdd(position, 0.95, 0.95, 0.95);
		if (this.dotselectors[7]) this.targetPoints[targetSize++].setAdd(position, 0.95, 0.05, 0.95);
		if (this.dotselectors[8]) this.targetPoints[targetSize++].setAdd(position, 0.05, 0.5, 0.5);
		if (this.dotselectors[9]) this.targetPoints[targetSize++].setAdd(position, 0.5, 0.05, 0.5);
		if (this.dotselectors[10]) this.targetPoints[targetSize++].setAdd(position, 0.5, 0.5, 0.05);
		if (this.dotselectors[11]) this.targetPoints[targetSize++].setAdd(position, 0.95, 0.5, 0.5);
		if (this.dotselectors[12]) this.targetPoints[targetSize++].setAdd(position, 0.5, 0.95, 0.5);
		if (this.dotselectors[13]) this.targetPoints[targetSize++].setAdd(position, 0.5, 0.5, 0.95);

		return isVisible(viewerPosition, this.targetPoints, targetSize);
	}

	private boolean rayIntersection(int[] b, Vec3d rayOrigin, Vec3d rayDir) {
		Vec3d rInv = new Vec3d(1, 1, 1).div(rayDir);

		double t1 = (b[0] - rayOrigin.x) * rInv.x;
		double t2 = (b[0] + 1 - rayOrigin.x) * rInv.x;
		double t3 = (b[1] - rayOrigin.y) * rInv.y;
		double t4 = (b[1] + 1 - rayOrigin.y) * rInv.y;
		double t5 = (b[2] - rayOrigin.z) * rInv.z;
		double t6 = (b[2] + 1 - rayOrigin.z) * rInv.z;

		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

		if (tmax > 0) {
			return false;
		}
		if (tmin > tmax) {
			return false;
		}

		return true;
	}

	private boolean isVisible(Vec3d start, Vec3d[] targets, int size) {
		int x = this.cameraPos[0];
		int y = this.cameraPos[1];
		int z = this.cameraPos[2];

		for (int v = 0; v < size; v++) {
			Vec3d target = targets[v];

			double relativeX = start.x - target.getX();
			double relativeY = start.y - target.getY();
			double relativeZ = start.z - target.getZ();

			if (this.allowRayChecks && rayIntersection(this.lastHitBlock, start, new Vec3d(relativeX, relativeY, relativeZ).normalize())) {
				continue;
			}

			double dimensionX = Math.abs(relativeX);
			double dimensionY = Math.abs(relativeY);
			double dimensionZ = Math.abs(relativeZ);

			double dimFracX = 1.0 / dimensionX;
			double dimFracY = 1.0 / dimensionY;
			double dimFracZ = 1.0 / dimensionZ;

			int intersectCount = 1;
			int x_inc, y_inc, z_inc;
			double t_next_y, t_next_x, t_next_z;

			if (dimensionX == 0.0) {
				x_inc = 0;
				t_next_x = dimFracX;
			} else if (target.x > start.x) {
				x_inc = 1;
				intersectCount += MathUtilities.floor(target.x) - x;
				t_next_x = (x + 1 - start.x) * dimFracX;
			} else {
				x_inc = -1;
				intersectCount += x - MathUtilities.floor(target.x);
				t_next_x = (start.x - x) * dimFracX;
			}

			if (dimensionY == 0.0) {
				y_inc = 0;
				t_next_y = dimFracY;
			} else if (target.y > start.y) {
				y_inc = 1;
				intersectCount += MathUtilities.floor(target.y) - y;
				t_next_y = (y + 1 - start.y) * dimFracY;
			} else {
				y_inc = -1;
				intersectCount += y - MathUtilities.floor(target.y);
				t_next_y = (start.y - y) * dimFracY;
			}

			if (dimensionZ == 0.0) {
				z_inc = 0;
				t_next_z = dimFracZ;
			} else if (target.z > start.z) {
				z_inc = 1;
				intersectCount += MathUtilities.floor(target.z) - z;
				t_next_z = (z + 1 - start.z) * dimFracZ;
			} else {
				z_inc = -1;
				intersectCount += z - MathUtilities.floor(target.z);
				t_next_z = (start.z - z) * dimFracZ;
			}

			boolean finished = stepRay(start, x, y, z, dimFracX, dimFracY, dimFracZ, intersectCount, x_inc, y_inc, z_inc, t_next_y, t_next_x, t_next_z);
			this.provider.cleanup();
			if (finished) {
				cacheResult(targets[0], true);
				return true;
			} else {
				this.allowRayChecks = true;
			}
		}
		cacheResult(targets[0], false);
		return false;
	}

	private boolean stepRay(Vec3d start, int currentX, int currentY, int currentZ, double distInX, double distInY, double distInZ, int n, int x_inc, int y_inc, int z_inc, double t_next_y, double t_next_x, double t_next_z) {
		this.allowWallClipping = true;
		for (; n > 1; n--) {
			int cVal = getCacheValue(currentX, currentY, currentZ);

			if (cVal == 2 && !this.allowWallClipping) {
				this.lastHitBlock[0] = currentX;
				this.lastHitBlock[1] = currentY;
				this.lastHitBlock[2] = currentZ;
				return false;
			}

			if (cVal == 0) {
				int chunkX = currentX >> 4;
				int chunkZ = currentZ >> 4;
				if (!this.provider.prepareChunk(chunkX, chunkZ)) {
					return false;
				}

				if (this.provider.isOpaqueFullCube(currentX, currentY, currentZ)) {
					if (!this.allowWallClipping) {
						this.cache.setLastHidden();
						this.lastHitBlock[0] = currentX;
						this.lastHitBlock[1] = currentY;
						this.lastHitBlock[2] = currentZ;
						return false;
					}
				} else {
					this.allowWallClipping = false;
					this.cache.setLastVisible();
				}
			}

			if (cVal == 1) {
				this.allowWallClipping = false;
			}

			if (t_next_y < t_next_x && t_next_y < t_next_z) {
				currentY += y_inc;
				t_next_y += distInY;
			} else if (t_next_x < t_next_y && t_next_x < t_next_z) {
				currentX += x_inc;
				t_next_x += distInX;
			} else {
				currentZ += z_inc;
				t_next_z += distInZ;
			}
		}
		return true;
	}

	private int getCacheValue(int x, int y, int z) {
		x -= this.cameraPos[0];
		y -= this.cameraPos[1];
		z -= this.cameraPos[2];
		if (Math.abs(x) > this.reach - 2 || Math.abs(y) > this.reach - 2 || Math.abs(z) > this.reach - 2) {
			return -1;
		}
		return this.cache.getState(x + this.reach, y + this.reach, z + this.reach);
	}

	private void cacheResult(Vec3d vector, boolean result) {
		int cx = MathUtilities.floor(vector.x) - this.cameraPos[0] + this.reach;
		int cy = MathUtilities.floor(vector.y) - this.cameraPos[1] + this.reach;
		int cz = MathUtilities.floor(vector.z) - this.cameraPos[2] + this.reach;
		if (result) {
			this.cache.setVisible(cx, cy, cz);
		} else {
			this.cache.setHidden(cx, cy, cz);
		}
	}

	public void resetCache() {
		this.cache.resetCache();
	}

	private enum Relative {
		INSIDE, POSITIVE, NEGATIVE;

		public static Relative from(int min, int max, int pos) {
			if (max > pos && min > pos) {
				return POSITIVE;
			} else if (min < pos && max < pos) {
				return NEGATIVE;
			}
			return INSIDE;
		}
	}

}