package silence.simsool.lucentclient.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.world.phys.shapes.VoxelShape;

public class RenderUtils {

	public static void fillShape(
			final PoseStack poseStack,
			final VertexConsumer builder,
			final VoxelShape shape,
			final double x,
			final double y,
			final double z,
			final int color
		) {
			PoseStack.Pose pose = poseStack.last();

			shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
				addBox(
					pose,
					builder,
					(float)(x1 + x), (float)(y1 + y), (float)(z1 + z),
					(float)(x2 + x), (float)(y2 + y), (float)(z2 + z),
					color
				);
			});
		}

		private static void addBox(
			PoseStack.Pose pose,
			VertexConsumer builder,
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			int color
		) {
			// bottom
			vertex(builder, pose, x1,y1,z1, color);
			vertex(builder, pose, x2,y1,z1, color);
			vertex(builder, pose, x2,y1,z2, color);
			vertex(builder, pose, x1,y1,z2, color);

			// top
			vertex(builder, pose, x1,y2,z1, color);
			vertex(builder, pose, x1,y2,z2, color);
			vertex(builder, pose, x2,y2,z2, color);
			vertex(builder, pose, x2,y2,z1, color);

			// north
			vertex(builder, pose, x1,y1,z1, color);
			vertex(builder, pose, x1,y2,z1, color);
			vertex(builder, pose, x2,y2,z1, color);
			vertex(builder, pose, x2,y1,z1, color);

			// south
			vertex(builder, pose, x1,y1,z2, color);
			vertex(builder, pose, x2,y1,z2, color);
			vertex(builder, pose, x2,y2,z2, color);
			vertex(builder, pose, x1,y2,z2, color);

			// west
			vertex(builder, pose, x1,y1,z1, color);
			vertex(builder, pose, x1,y1,z2, color);
			vertex(builder, pose, x1,y2,z2, color);
			vertex(builder, pose, x1,y2,z1, color);

			// east
			vertex(builder, pose, x2,y1,z1, color);
			vertex(builder, pose, x2,y2,z1, color);
			vertex(builder, pose, x2,y2,z2, color);
			vertex(builder, pose, x2,y1,z2, color);
		}

		private static void vertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z, int color) {
			builder.addVertex(pose, x, y, z)
			.setColor(color)
			.setNormal(pose, 0, 1, 0)
			.setLineWidth(1.0f);
		}

}