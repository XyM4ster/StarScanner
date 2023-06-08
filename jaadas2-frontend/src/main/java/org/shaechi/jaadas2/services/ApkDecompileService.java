package org.shaechi.jaadas2.services;

import java.io.IOException;

public interface ApkDecompileService {
    public void parallelDecompile(String jar_name) throws IOException;
    public int getState();
}
