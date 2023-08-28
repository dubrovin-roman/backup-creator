package ru.backup;

import java.nio.file.Path;

public class DataForBackupCreatorDTO {
    private Path pathIn;
    private Path pathOutWithFileName;

    public DataForBackupCreatorDTO(Path pathIn, Path pathOutWithFileName) {
        this.pathIn = pathIn;
        this.pathOutWithFileName = pathOutWithFileName;
    }

    public Path getPathIn() {
        return pathIn;
    }

    public void setPathIn(Path pathIn) {
        this.pathIn = pathIn;
    }

    public Path getPathOutWithFileName() {
        return pathOutWithFileName;
    }

    public void setPathOutWithFileName(Path pathOutWithFileName) {
        this.pathOutWithFileName = pathOutWithFileName;
    }
}
