package org.artifex.testing;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ParseShader
{

    public ParseShader(File file){
        this.file=file;
    }


    private void init() throws IOException {

        Scanner scanner = new Scanner(file);
    }

    private final File file;
}
