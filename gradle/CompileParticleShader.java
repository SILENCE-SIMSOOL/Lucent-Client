import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.util.shaderc.Shaderc;

public class CompileParticleShader {

	public static void main(String[] args) throws Exception {
		Path source = Path.of(args[0]);
		Path output = Path.of(args[1]);
		long compiler = Shaderc.shaderc_compiler_initialize();
		if (compiler == 0) {
			throw new IllegalStateException("Cannot initialize shaderc");
		}
		long options = Shaderc.shaderc_compile_options_initialize();
		try {
			if (options == 0) {
				throw new IllegalStateException("Cannot initialize shaderc options");
			}
			Shaderc.shaderc_compile_options_set_target_env(options, Shaderc.shaderc_target_env_vulkan, Shaderc.shaderc_env_version_vulkan_1_0);
			long result = Shaderc.shaderc_compile_into_spv(compiler, Files.readString(source), Shaderc.shaderc_compute_shader, source.getFileName().toString(), "main", options);
			if (result == 0) {
				throw new IllegalStateException("No shaderc compilation result");
			}
			try {
				if (Shaderc.shaderc_result_get_compilation_status(result) != Shaderc.shaderc_compilation_status_success) {
					throw new IllegalStateException(Shaderc.shaderc_result_get_error_message(result));
				}
				ByteBuffer bytes = Shaderc.shaderc_result_get_bytes(result);
				byte[] spirv = new byte[bytes.remaining()];
				bytes.get(spirv);
				Files.createDirectories(output.getParent());
				Files.write(output, spirv);
			} finally {
				Shaderc.shaderc_result_release(result);
			}
		} finally {
			if (options != 0) {
				Shaderc.shaderc_compile_options_release(options);
			}
			Shaderc.shaderc_compiler_release(compiler);
		}
	}

}