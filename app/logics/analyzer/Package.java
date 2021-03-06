package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Package implements Component {

    private Features features;
    private List<Component> componentList = new ArrayList<>();

    public Package(Features current) {
        features = current;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    @Override
    public boolean add(String search, Path f, String remainPath) {
        if (features.getPath().equals(search)) {
            if (remainPath.indexOf('/') != -1) {
                String toSearch = features.getPath() + "/" + remainPath.substring(0, remainPath.indexOf('/'));
                String remain = remainPath.substring(remainPath.indexOf('/') + 1);
                for (Component c : componentList) {
                    if (c.add(toSearch, f, remain)) {
                        return true;
                    }
                }

                Package p = new Package(new Features(toSearch.substring(toSearch.lastIndexOf('/') + 1), toSearch, f));
                p.add(toSearch, f, remain);
                componentList.add(p);


            } else {
                String name = remainPath;
                //add file
                if (isTextFile(f)) {
                    DataFile file = new DataFile(new Features(name, this.features.getPath() + "/" + remainPath, f));
                    componentList.add(file);
                } else {
                    BinaryFile file = new BinaryFile(new Features(name, this.features.getPath() + "/" + remainPath, f));
                    componentList.add(file);

                }
            }

            return true;
        } else {
            return false;
        }
    }


    @Override
    public RenderChild getRenderJSON() {
        RenderChild[] renderComponent = componentList.stream().map((x) -> x.getRenderJSON()).toArray(x -> new RenderChild[x]);

        features.setColorMetrics(safeNumber(features.getColorMetrics()));
        features.setColor(safeNumber(features.getColor()));

        return new RenderChild(new float[]{safeNumber(features.getRendererLeft()), 0, safeNumber(features.getRendererTop())}, new RenderComponent(features, renderComponent));
    }


    @Override
    public Features getFeatures() {
        return this.features;
    }

    @Override
    public <T> T applyFunction(Function<Component, T> function) {
        return function.apply(this);
    }

    private boolean isTextFile(Path p) {
        try (Stream<String> fileLinesStream = Files.lines(p)) {
            fileLinesStream.count();
            return true;

        } catch (Exception e) {
            return false;
        }

    }

}



