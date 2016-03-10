package logics.analyzer.analysis;

import exception.CustumException;
import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.*;
import logics.analyzer.Package;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by bedux on 03/03/16.
 */
public class WordCountAnalyser implements Analyser<Integer> {

    @Override
    public Integer analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new WordCountAnalyser())::analysis));
        int n = 0;
        if (c instanceof BinaryFile) {
            n= 10;
        } else if (c instanceof DataFile) {
            n=analysisCast((DataFile)c);
        }else if (c instanceof logics.analyzer.Package){
            n=analysisCast((Package) c);
        }
        c.getFeatures().setWordCount(n);
        return n;

    }


    private Integer analysisCast(Package p){
        return 10;
    }

    private Integer analysisCast(DataFile c){
        try (Stream<String> fileLinesStream = Files.lines(c.getFeatures().getFilePath())) {
            int wc = (fileLinesStream.filter((line) -> {
                return line.replace("\\s+", "").length() > 0;
            }).mapToInt((line) -> {
                line = line.trim();
                return line.split("\\s+").length;
            }).sum());
            return wc;
        } catch (IOException e) {
            throw new CustumException(e);
        }
    }
}
