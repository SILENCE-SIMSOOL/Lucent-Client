package silence.simsool.lucentclient.init;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class LucentMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

//	private void replaceStateHolderValuesType(ClassNode targetClass) {
//		final String oldType = "it/unimi/dsi/fastutil/objects/Reference2ObjectArrayMap";
//		final String newType = "it/unimi/dsi/fastutil/objects/Reference2ObjectMap";
//		final String fieldNameToReplace = FabricLoader.getInstance().getMappingResolver().mapFieldName(
//				"official",
//				"net.minecraft.world.level.block.state.StateHolder",
//				"values",
//				"Lit/unimi/dsi/fastutil/objects/Reference2ObjectArrayMap;"
//		);
//
//		FieldNode valuesFieldNode = getFieldNode(targetClass, fieldNameToReplace);
//		valuesFieldNode.desc = valuesFieldNode.desc.replace(oldType, newType);
//		if (valuesFieldNode.signature != null) {
//			valuesFieldNode.signature = valuesFieldNode.signature.replace(oldType, newType);
//		}
//
//		for (MethodNode method : targetClass.methods) {
//			for (AbstractInsnNode insn : method.instructions) {
//				if (insn instanceof FieldInsnNode fieldInsn && fieldInsn.name.equals(fieldNameToReplace)) {
//					fieldInsn.desc = fieldInsn.desc.replace(oldType, newType);
//				}
//				else if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL && insn instanceof MethodInsnNode call) {
//					if (call.owner.contains(oldType)) {
//						call.owner = call.owner.replace(oldType, newType);
//						call.setOpcode(Opcodes.INVOKEINTERFACE);
//						call.itf = true;
//					}
//				}
//				else if (insn.getOpcode() == Opcodes.CHECKCAST && insn instanceof TypeInsnNode cast) {
//					cast.desc = cast.desc.replace(oldType, newType);
//				}
//			}
//		}
//	}

//	private FieldNode getFieldNode(ClassNode clazz, String fieldName) {
//		for (FieldNode field : clazz.fields) {
//			if (field.name.equals(fieldName)) {
//				return field;
//			}
//		}
//		String fields = clazz.fields.stream()
//				.map(n -> n.name)
//				.reduce((s1, s2) -> s1 + ", " + s2)
//				.orElse("[None]");
//		throw new RuntimeException(
//				"Failed to find field with name " + fieldName + " in " + clazz.name + ", available fields are " + fields
//		);
//	}

}