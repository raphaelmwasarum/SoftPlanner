package com.raphjava.softplanner.data.services;//package data.services;
//
//import data.interfaces.IOService;
//import data.proxies.*;
//import net.raphjava.raphtility.collectionmanipulation.ArrayList;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
//import net.raphjava.raphtility.logging.interfaces.Log;
//
//import java.io.*;
//import java.util.Collection;
//import java.util.Objects;
//import java.util.function.Consumer;
//
//public class DefaultIOService implements IOService
//{
//    private StudeeFileBuilder fileBuilder;
//    private Log logger;
//
//    private DefaultIOService(Builder builder)
//    {
//        fileBuilder = builder.fileBuilder;
//        logger = builder.logger;
//    }
//
//    public static Builder newBuilder()
//    {
//        return new Builder();
//    }
//
// /*   public DefaultIOService(LoggerFactory loggerFactory, StudeeFileBuilder fileBuilder)
//    {
//        logger = loggerFactory.createLogger(getClass().getSimpleName());
//        this.fileBuilder = fileBuilder;
////        fileBuilder.setRoot(context.getFilesDir() + File.separator + "user_data");
////        setupUnitDataDirectory();
//    }*/
//
//
//
//   /* @Override
//    public Bitmap getImageFromFile(String absoluteFilePath)
//    {
////        String ab = getImageAbsolutePath(imageName);
////        return BitmapFactory.decodeFile(ab);
//        return BitmapFactory.decodeFile(absoluteFilePath);
//    }*/
//
//
//    @Override
//    public String getTopicContent(TopicProxy topic)
//    {
//        String path = getTopicTextFilePath(topic);
//        return read(path);
//    }
//
//    public String read(String path)
//    {
//        try (BufferedReader br = new BufferedReader(new FileReader(path)))
//        {
//            StringBuilder sb = new StringBuilder();
//            String data = br.readLine();
//            String line = (data != null ? (data + "\n") : null);
//            while (line != null)
//            {
//                sb.append(line);
//                data = br.readLine();
//                line = (data != null ? (data + "\n") : null);
//            }
//            return sb.toString();
//
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public String getQuestionContent(QuestionProxy question)
//    {
//        String path = getQuestionTextFilePath(question);
//        return read(path);
//    }
//
//    @Override
//    public String getAnswerContent(AnswerProxy answer)
//    {
//        return read(getAnswerTextFilePath(answer));
//    }
//
//    @Override
//    public String getTopicTextDirectory(TopicProxy topic)
//    {
//        return fileBuilder.getTopicTextDirectory(getFolderName(topic.getUnit().getResources()), getFolderName(topic.getResources()));
//    }
//
//    @Override
//    public boolean ensureExistence(String directory)
//    {
//        try
//        {
//            File f = new File(directory);
//            if (!f.exists()) return f.mkdirs();
//            return true;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    /**
//     * Overwrites to file. By default, this method creates the file if it doesn't exist.
//     *
//     * @param filePath     file's path.
//     * @param data         data.
//     * @param failIfAbsent doesn't create the file if it doesn't exist.
//     * @return true if writing was successful, false otherwise.
//     */
//    @Override
//    public boolean writeToFile(String filePath, String data, boolean... failIfAbsent)
//    {
//        if (failIfAbsent.length != 0 && failIfAbsent[0])
//        {
//            if (new File(filePath).exists()) return postValidationWrite(filePath, data);
//            else return false;
//        }
//        else
//        {
//            return postValidationWrite(filePath, data);
//
//        }
//    }
//
//    @Override
//    public String getCssFilePath(UnitProxy proxy)
//    {
//        OwnershipProxy css = Objects.requireNonNull(asExp(proxy.getResources()).firstOrDefault(o -> o.getResource() != null &&
//                o.getResource().getName().equals(Resource.CSS)), String.format("Couldn't find the css file for unit %s",
//                proxy.getUnit().getName()));
//
//        return String.format("%s%s%s", getTextDirectory(proxy), File.separator, css.getResource().getContent());
//
//    }
//
//    @Override
//    public String getCssFilePath(TopicProxy proxy)
//    {
//        return getCssFilePath(Objects.requireNonNull(proxy.getUnit(),
//                String.format("Topic must have a unit. topic details: %s", proxy.getName())));
//    }
//
//    @Override
//    public String getCssFilePath(QuestionProxy proxy)
//    {
//        return getCssFilePath(Objects.requireNonNull(proxy.getTopic(),
//                String.format("Question must have a topic. Question details: %s", proxy.getDescription())));
//    }
//
//    @Override
//    public String getCssFilePath(AnswerProxy proxy)
//    {
//        return getCssFilePath(Objects.requireNonNull(proxy.getQuestion(),
//                String.format("Answer must have a question. Answer details: %s", proxy.getDescription())));
//    }
//
//    @Override
//    public String getTextDirectory(UnitProxy proxy)
//    {
//        return fileBuilder.getUnitTextDirectory(getFolderName(proxy.getResources()));
//    }
//
//    @Override
//    public String getTextDirectory(TopicProxy proxy)
//    {
//        return getTopicTextDirectory(proxy);
//    }
//
//    @Override
//    public String getTextDirectory(QuestionProxy proxy)
//    {
//        return getQuestionTextDirectory(proxy);
//    }
//
//    @Override
//    public String getTextDirectory(AnswerProxy proxy)
//    {
//        return getAnswerTextDirectory(proxy);
//    }
//
//
//    @Override
//    public String getTextFilePath(TopicProxy proxy)
//    {
//        return getTopicTextFilePath(proxy);
//    }
//
//    @Override
//    public String getTextFilePath(QuestionProxy proxy)
//    {
//        return getQuestionTextFilePath(proxy);
//    }
//
//    @Override
//    public String getTextFilePath(AnswerProxy proxy)
//    {
//        return getAnswerTextFilePath(proxy);
//    }
//
//    private boolean postValidationWrite(String filePath, String data)
//    {
//        try (FileWriter fs = new FileWriter(new File(filePath)))
//        {
//            fs.write(data);
//            fs.flush();
//            return true;
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//    String getQuestionTextDirectory(QuestionProxy question)
//    {
//        TopicProxy topic = question.getTopic();
//        Objects.requireNonNull(topic, "Failed to find topic associated with question " + question.getDescription());
//        UnitProxy unit = topic.getUnit();
//        Objects.requireNonNull(unit, "Failed to find unit associated with question " + question.getDescription());
//        return fileBuilder.getQuestionTextDirectory
//                (
//                        getFolderName(unit.getResources()),
//                        getFolderName(topic.getResources()),
//                        getFolderName(question.getResources())
//                );
//    }
//
//     private String getQuestionTextFilePath(QuestionProxy question)
//    {
//        return getQuestionTextDirectory(question) + File.separator + getFileName(question.getResources());
//    }
//
//     private String getAnswerTextFilePath(AnswerProxy answer)
//    {
//        return getAnswerTextDirectory(answer) + File.separator + getFileName(answer.getResources());
//    }
//
//    private String getAnswerTextDirectory(AnswerProxy answer)
//    {
//        QuestionProxy question = answer.getQuestion();
//        Objects.requireNonNull(question, "Failed to find question associated with answer " + answer.getDescription());
//        TopicProxy topic = question.getTopic();
//        Objects.requireNonNull(topic, "Failed to find topic associated with answer " + answer.getDescription());
//        UnitProxy unit = topic.getUnit();
//        Objects.requireNonNull(unit, "Failed to find unit associated with answer " + answer.getDescription());
//        return fileBuilder.getAnswerTextDirectory
//                (
//                        getFolderName(unit.getResources()),
//                        getFolderName(topic.getResources()),
//                        getFolderName(question.getResources()),
//                        getFolderName(answer.getResources())
//                );
//    }
//
//    private String getTopicTextFilePath(TopicProxy topic)
//    {
//        UnitProxy unit = topic.getUnit();
//        Objects.requireNonNull(unit, "Failed to find unit associated with question " + topic.getName());
//        return fileBuilder.getTopicTextDirectory
//                (
//                        getFolderName(unit.getResources()),
//                        getFolderName(topic.getResources())
//
//                ) + File.separator + getFileName(topic.getResources());
//
//    }
//
//    private String getFileName(Collection<OwnershipProxy> resources)
//    {
//        OwnershipProxy resource = asExp(resources).firstOrDefault(o -> o.getResource().getName().equals(Resource.HTML));
//        if (resource == null || resource.getResource() == null)
//            throw new RuntimeException("Entity doesn't have directory data. Check entities that have " +
//                    "associations with Resource.Directory.");
//        return resource.getResource().getContent();
//    }
//
//    private String getTopicsDirectory(TopicProxy topic)
//    {
//        return fileBuilder.getUnitTopicsDirectory(getFolderName(topic.getUnit().getResources()));
//    }
//
//    @Override
//    public String getImageFilePath(UnitProxy unit)
//    {
//        LambdaDataHolder<String> unitDirectoryBox = new LambdaDataHolder<>();
//        LambdaDataHolder<String> imageNameBox = new LambdaDataHolder<>();
//        extractResourceContent(unitDirectoryBox, imageNameBox, unit);
//        return fileBuilder.getUnitImagesDirectory(unitDirectoryBox.getData())
//                + File.separator + imageNameBox.getData();
//    }
//
//    @Override
//    public String getImagesDirectory(UnitProxy unit)
//    {
//        return fileBuilder.getUnitImagesDirectory(getFolderName(unit.getResources()));
//    }
//
//    @Override
//    public String getImagesDirectory(TopicProxy topic)
//    {
//        return fileBuilder.getTopicImagesDirectory(getFolderName(topic.getUnit().getResources())
//                , getFolderName(topic.getResources()));
//    }
//
//    @Override
//    public String getImagesDirectory(QuestionProxy question)
//    {
//        return fileBuilder.getQuestionImagesDirectory(
//                getFolderName(question.getTopic().getUnit().getResources()),
//                getFolderName(question.getTopic().getResources()),
//                getFolderName(question.getResources()));
//    }
//
//    @Override
//    public String getImagesDirectory(AnswerProxy answer)
//    {
//        return fileBuilder.getAnswerImagesDirectory(
//                getFolderName(answer.getQuestion().getTopic().getUnit().getResources()),
//                getFolderName(answer.getQuestion().getTopic().getResources()),
//                getFolderName(answer.getQuestion().getResources()),
//                getFolderName(answer.getResources()));
//    }
//
//
//    private String getFolderName(Collection<OwnershipProxy> resources)
//    {
//        OwnershipProxy resource = asExp(resources).firstOrDefault(o -> o.getResource().getName().equals(Resource.DIRECTORY));
//        if (resource == null || resource.getResource() == null)
//            throw new RuntimeException("Entity doesn't have directory data. Check entities that have " +
//                    "associations with Resource.Directory.");
//        return resource.getResource().getContent();
//    }
//
//    private void extractResourceContent(LambdaDataHolder<String> unitDirectoryBox, LambdaDataHolder<String> imageNameBox, UnitProxy unit)
//    {
//        Consumer<ResourceProxy> ensure = r ->
//        {
//            if (r == null) throw new RuntimeException("Failed to get unit image data.");
//            if (r.getName() == null) throw new RuntimeException("Failed to get unit image data.");
//            if (r.getContent() == null)
//                throw new RuntimeException("Failed to get unit image data.");
//        };
//        int dataCounter = 0;
//        for (OwnershipProxy o : unit.getResources())
//        {
//            ensure.accept(o.getResource());
//            if (o.getResource().getName().equals(Resource.DIRECTORY))
//            {
//                unitDirectoryBox.setData(o.getResource().getContent());
//                dataCounter++;
//                if (imageNameBox.getData() != null) break;
//            }
//            else if (o.getResource().getName().equals(Resource.IMAGE))
//            {
//                imageNameBox.setData(o.getResource().getContent());
//                dataCounter++;
//                if (unitDirectoryBox.getData() != null) break;
//            }
//        }
//        if (dataCounter != 2) throw new RuntimeException("Failed to get unit image data.");
//    }
//
//    private <T> Explorable<T> asExp(Collection<T> resources)
//    {
//        return new ArrayList<>(resources);
//    }
//
//    private void setupUnitDataDirectory()
//    {
//        LambdaDataHolder<Consumer<File>> deleterBridge = new LambdaDataHolder<>(); /*To enable access deleter inside itself - recursion.*/
//        Consumer<File> deleter = f ->
//        {
//            File[] files = f.listFiles();
//            if (files != null)
//            {
//                for (File file : files) deleterBridge.getData().accept(file);
//            }
//            if (!f.delete()) throw new RuntimeException("Failed to delete stale unit data.");
//        };
//        deleterBridge.setData(deleter);
//
//        Consumer<File> mkDirs = f ->
//        {
//            if (!f.mkdirs())
//                throw new RuntimeException("Failed to create directory: " + f.getName());
//            logger.debug("Creating file: " + f.getName() + " successful.");
//        };
//
//        Consumer<File> resetDirectory = d ->
//        {
//            if (d.exists()) deleter.accept(d);
//            else logger.debug("Unit data directory already does not exist. Creating file...");
//            mkDirs.accept(d);
//        };
//
//        File unitDir = new File(fileBuilder.getUnitsDirectory());
//        resetDirectory.accept(unitDir);
//
//    }
//
//    public static final class Builder
//    {
//        private StudeeFileBuilder fileBuilder;
//        private Log logger;
//
//        private Builder(StudeeFileBuilder fileBuilder)
//        {
//            this.fileBuilder = fileBuilder;
//        }
//
//        private Builder()
//        {
//        }
//
//        public Builder logger(Log logger)
//        {
//            this.logger = logger;
//            return this;
//        }
//
//        public DefaultIOService build()
//        {
//            return new DefaultIOService(this);
//        }
//
//        public Builder fileBuilder(StudeeFileBuilder fileBuilder)
//        {
//            this.fileBuilder = fileBuilder;
//            return this;
//        }
//    }
//}
