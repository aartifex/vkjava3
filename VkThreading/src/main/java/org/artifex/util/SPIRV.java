package org.artifex.util;

import org.lwjgl.system.NativeResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemClassLoader;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.util.shaderc.Shaderc.*;

public class SPIRV implements NativeResource
{
    public static SPIRV compileShaderFile(String shaderFile, ShaderType shaderType) {
        return compileShaderAbsoluteFile(getSystemClassLoader().getResource(shaderFile).toExternalForm(), shaderType);
    }

    public static SPIRV compileShaderAbsoluteFile(String shaderFile, ShaderType shaderType) {
        try {
            String source = new String(Files.readAllBytes(Paths.get(new URI(shaderFile))));
            return compileShader(shaderFile, source, shaderType);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SPIRV compileShader(String filename, String source, ShaderType shaderType) {

        long compiler = shaderc_compiler_initialize();

        if(compiler == NULL) {
            throw new RuntimeException("Failed to create shader compiler");
        }

        long result = shaderc_compile_into_spv(compiler, source, shaderType.type, filename, "main", NULL);

        if(result == NULL) {
            throw new RuntimeException("Failed to compile shader " + filename + " into SPIR-V");
        }

        if(shaderc_result_get_compilation_status(result) != shaderc_compilation_status_success) {
            throw new RuntimeException("Failed to compile shader " + filename + "into SPIR-V:\n " + shaderc_result_get_error_message(result));
        }

        shaderc_compiler_release(compiler);

        return new SPIRV(result, shaderc_result_get_bytes(result));
    }

    public enum ShaderType {

        VERTEX_SHADER(shaderc_glsl_vertex_shader),
        GEOMETRY_SHADER(shaderc_glsl_geometry_shader),
        FRAGMENT_SHADER(shaderc_glsl_fragment_shader),
        COMPUTE_SHADER(shaderc_glsl_compute_shader);

        public final int type;

        ShaderType(int type) {
            this.type = type;
        }
    }

    private final long handle;
    private ByteBuffer bytecode;

    public SPIRV(long handle, ByteBuffer bytecode) {
        this.handle = handle;
        this.bytecode = bytecode;
    }

    public ByteBuffer bytecode() {
        return bytecode;
    }

    @Override
    public void free() {
        shaderc_result_release(handle);
        bytecode = null; // Help the GC
    }


}
