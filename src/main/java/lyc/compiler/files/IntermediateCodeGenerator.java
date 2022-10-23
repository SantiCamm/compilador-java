package lyc.compiler.files;

import lyc.compiler.terceto.*;

import java.io.FileWriter;
import java.io.IOException;

public class IntermediateCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("+------------------------------- TERCETOS -------------------------------+\n");

        for(Terceto terceto : TercetoManager.tercetoList ){
            fileWriter.write(terceto.getNumber() + "." + " " + terceto.getTerceto() + "\n");
        }
    }
}
