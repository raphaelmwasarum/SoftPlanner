package com.raphjava.softplanner.data.services;

import net.raphjava.qumbuqa.commons.trees.Tree;
import net.raphjava.qumbuqa.commons.trees.TreeNodeImp;
import net.raphjava.qumbuqa.commons.trees.TreeVisitor;
import net.raphjava.qumbuqa.commons.trees.interfaces.TreeNode;
import net.raphjava.raphtility.asynchrony.Task;
import net.raphjava.raphtility.asynchrony.TaskResult;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
import net.raphjava.raphtility.collections.generic.MultiValueHashMap;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class StudeeFileBuilder
{

    private static final String TEXT = "text";
    private static final String IMAGES = "images";
    private static final String UNIT_DIRECTORY_PLACE_HOLDER = "<UNIT_X_FOLDER>";
    private static final String TOPICS = "topics";
    private static final String TOPIC_DIRECTORY_PLACE_HOLDER = "<TOPIC_X_FOLDER>";
    private static final String QUESTIONS = "questions";
    private static final String QUESTION_DIRECTORY_PLACE_HOLDER = "<QUESTION_X_FOLDER>";
    private static final String ANSWERS = "answers";
    private static final String ANSWER_DIRECTORY_PLACE_HOLDER = "<ANSWER_X_FOLDER>";
    private Map<Entity, String> mainPaths = new HashMap<>();
    private MultiValueHashMap<Entity, Map.Entry<String, String>> pathMap = new MultiValueHashMap<>();
    private String root;
    private TreeNode<Folder> unitXFolder;
    private TreeNode<Folder> topicXFolder;
    private TreeNode<Folder> questionXFolder;
    private TreeNode<Folder> answerXFolder;
    private TreeNode<Folder> unitsFolder;

    private StudeeFileBuilder(Builder builder)
    {
        setRoot(builder.root);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public void setRoot(String root)
    {
        this.root = root;
        if(fileTree != null) fileTree.getRoot().getValue().setName(root);
    }

    public static void main(String[] args)
    {
        StudeeFileBuilder fm = StudeeFileBuilder.newBuilder()
                .root("studee_data")
                .build();


        System.out.println("\n\n***** DIRECTORY DEFINITION ****\n\n");
        new TreeVisitor.Builder<Folder>().tree(fm.fileTree).actionOnTableNode(n ->
        {
            if(n.getChildren().isEmpty())
            {
                StringBuilder sb = new StringBuilder();
                fm.climbTree(n).forEach(x -> x.append(sb));
                System.out.println(sb);
            }

        }).build().visit();
        System.out.println("\n\n***** DIRECTORY DEFINITION ****\n\n");


        System.out.println("\n\n***** DIRECTORY DEFINITION MULTITHREADING ****\n\n");
        Supplier<Supplier<String>> definerMaker = () -> () ->
               {
                   String threadName = Thread.currentThread().getName();
                   StringBuilder sb = new StringBuilder();
                   sb.append("\n\n***** path building test output ").append(threadName).append(" ****\n\n");

                   String unitFolder = "economics_" + threadName;
                   sb.append(fm.getUnitDirectory(unitFolder)).append("\n");
                   sb.append(fm.getUnitImagesDirectory(unitFolder)).append("\n");
                   sb.append(fm.getUnitTextDirectory(unitFolder)).append("\n");
                   sb.append(fm.getUnitTopicsDirectory(unitFolder)).append("\n");
                   try
                   {
                       Thread.sleep(200);
                   }
                   catch (InterruptedException e)
                   {
                       e.printStackTrace();
                   }
                   String topicFolder = "introduction_" + threadName;
                   sb.append(fm.getTopicDirectory(unitFolder, topicFolder)).append("\n");
                   sb.append(fm.getTopicTextDirectory(unitFolder, topicFolder)).append("\n");
                   sb.append(fm.getTopicImagesDirectory(unitFolder, topicFolder)).append("\n");
                   sb.append(fm.getTopicQuestionsDirectory(unitFolder, topicFolder)).append("\n");
                   String questionFolder = "question_" + threadName;
                   sb.append(fm.getQuestionDirectory(unitFolder, topicFolder, questionFolder)).append("\n");
                   sb.append(fm.getQuestionTextDirectory(unitFolder, topicFolder, questionFolder)).append("\n");
                   sb.append(fm.getQuestionImagesDirectory(unitFolder, topicFolder, questionFolder)).append("\n");
                   sb.append(fm.getQuestionAnswersDirectory(unitFolder, topicFolder, questionFolder)).append("\n");
                   String answerDirectory = "answer_" + threadName;
                   sb.append(fm.getAnswerDirectory(unitFolder, topicFolder, questionFolder, answerDirectory)).append("\n");
                   sb.append(fm.getAnswerTextDirectory(unitFolder, topicFolder, questionFolder, answerDirectory)).append("\n");
                   sb.append(fm.getAnswerImagesDirectory(unitFolder, topicFolder, questionFolder, answerDirectory)).append("\n");

                   sb.append("\n\n***** path building test output ").append(threadName).append(" ****\n\n");
                   return sb.toString();
               };

        CountDownLatch completionSignal = new CountDownLatch(3);
        TaskResult<String> t1 = new TaskResult<>(() -> definerMaker.get().get());
        t1.continueWith(t -> completionSignal.countDown());

        TaskResult<String> t2 = new TaskResult<>(() -> definerMaker.get().get());
        t2.continueWith(t -> completionSignal.countDown());

        TaskResult<String> t3 = new TaskResult<>(() -> definerMaker.get().get());
        t3.continueWith(t -> completionSignal.countDown());

        Task printer = new Task(() ->
        {
            t1.start();
            t2.start();
            t3.start();

            try
            {
                completionSignal.await();
                System.out.println(t1.getResult());
                System.out.println(t2.getResult());
                System.out.println(t3.getResult());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });

        printer.start();






        System.out.println("\n\n***** DIRECTORY DEFINITION MULTITHREADING ****\n\n");




        System.out.println("\n\n***** path building test output ****\n\n");

        String unitFolder = "economics_134214";
        System.out.println(fm.getUnitDirectory(unitFolder));
        System.out.println(fm.getUnitImagesDirectory(unitFolder));
        System.out.println(fm.getUnitTextDirectory(unitFolder));
        System.out.println(fm.getUnitTopicsDirectory(unitFolder));
        String topicFolder = "introduction_89903901";
        System.out.println(fm.getTopicDirectory(unitFolder, topicFolder));
        System.out.println(fm.getTopicTextDirectory(unitFolder, topicFolder));
        System.out.println(fm.getTopicImagesDirectory(unitFolder, topicFolder));
        System.out.println(fm.getTopicQuestionsDirectory(unitFolder, topicFolder));
        String questionFolder = "question_4312343";
        System.out.println(fm.getQuestionDirectory(unitFolder, topicFolder, questionFolder));
        System.out.println(fm.getQuestionTextDirectory(unitFolder, topicFolder, questionFolder));
        System.out.println(fm.getQuestionImagesDirectory(unitFolder, topicFolder, questionFolder));
        System.out.println(fm.getQuestionAnswersDirectory(unitFolder, topicFolder, questionFolder));
        String answerDirectory = "answer_312934";
        System.out.println(fm.getAnswerDirectory(unitFolder, topicFolder, questionFolder, answerDirectory));
        System.out.println(fm.getAnswerTextDirectory(unitFolder, topicFolder, questionFolder, answerDirectory));
        System.out.println(fm.getAnswerImagesDirectory(unitFolder, topicFolder, questionFolder, answerDirectory));

        System.out.println("\n\n***** path building test output ****\n\n");

    }

    private Collection<String> getPathTemplates()
    {
        ArrayList<String> l = new ArrayList<>(mainPaths.values());
        l.addAll(new net.raphjava.raphtility.collectionmanipulation.ArrayList<>(pathMap.values()).selectMany(c -> c).select(Map.Entry::getValue).list());
        return l;
    }

    private Tree<Folder> fileTree;

    public interface Folder
    {
        void setRoot(boolean isRoot);

        boolean isRoot();

        boolean isLeaf();

        void setName(String name);

        String name();

        void append(StringBuilder sb);

        void reset();

        void setInitialName(String name);

        String path();

        void setPath(String path);

        void setParentPath(Supplier<String> parentPath);
    }

    private void loadFileTree()
    {
        //unit_data
        root = root == null ? "user_data" : root;
        fileTree = new Tree<>(new TreeNodeImp<>(getFolderImp(true, root, null/*Never reached if it's the root. */)));

        //root_directory/units [...]
        addUnitsFolder(fileTree.getRoot());

        BiConsumer<TreeNode<Folder>, String> pathUpdater = (n, path) ->
        {
            if(!n.getChildren().isEmpty()) path += File.separator;
            n.getValue().setPath(path);
        };


        //Update path expressions.
        new TreeVisitor.Builder<Folder>().tree(fileTree).actionOnTableNode(n ->
        {
            if(n.isRoot())
            {
                pathUpdater.accept(n, n.getValue().name());
                return;
            }
            pathUpdater.accept(n, n.getParent().getValue().path() + File.separator);

        }).build().visit();


    }

    private Folder getFolderImp(boolean isRoot, String name, Supplier<String> parentPath)
    {
        Folder folder = new Folder()
        {
            private Supplier<String> parentPath;

            private String path;

            private Supplier isLeaf;
            private String name;
            private String initialName;
            private boolean root;


            public void setParentPath(Supplier<String> parentPath)
            {
                this.parentPath = parentPath;
            }

            public void setIsLeaf(Supplier isLeaf)
            {
                this.isLeaf = isLeaf;
            }

            public void setPath(String path)
            {
                this.path = path;
            }

            @Override
            public void setRoot(boolean isRoot)
            {
                root = isRoot;
            }

            @Override
            public boolean isRoot()
            {
                return root;
            }

            @Override
            public boolean isLeaf()
            {
                return (boolean) isLeaf.get();
            }

            @Override
            public void setName(String name)
            {
                this.name = name;
            }

            @Override
            public String name()
            {
                return name;
            }

            @Override
            public void append(StringBuilder sb)
            {
                if (!root) sb.append(File.separator);
                sb.append(name);
            }

            @Override
            public void reset()
            {
                name = initialName;
            }

            @Override
            public void setInitialName(String name)
            {
                initialName = name;
            }

            @Override
            public String path()
            {
                if(root) return name;
                return parentPath.get() + File.separator + name;
            }
        };

        folder.setParentPath(parentPath);

        folder.setRoot(isRoot);
        folder.setName(name);
        folder.setInitialName(name);

        return folder;
    }


    private void addUnitsFolder(TreeNode<Folder> root/* root_directory */)
    {

        //units
        unitsFolder = getFolder("units", () -> root.getValue().path());

        //root_directory/units/<UNIT_X_FOLDER> [...]
        addUnitXFolder(unitsFolder);


        //root_directory/units
        root.getChildren().add(unitsFolder);


    }

    private void addUnitXFolder(TreeNode<Folder> unitsFolder /* root_directory/units */)
    {
        //<UNIT_X_FOLDER>
        unitXFolder = getFolder(UNIT_DIRECTORY_PLACE_HOLDER, () -> unitsFolder.getValue().path());

        //<UNIT_X_FOLDER>/text
        addTextFolder(unitXFolder);

        //<UNIT_X_FOLDER>/topics [...]
        addTopicsFolder(unitXFolder);

        //<UNIT_X_FOLDER>/images
        addImagesFolder(unitXFolder);

        //root_directory/units/<UNIT_X_FOLDER>
        unitsFolder.getChildren().add(unitXFolder);
    }

    private void addImagesFolder(TreeNode<Folder> parentFolder)
    {
        //parentFolder/images
        parentFolder.getChildren().add(getFolder(IMAGES, () -> parentFolder.getValue().path()));
    }

    private void addTopicsFolder(TreeNode<Folder> parentFolder)
    {
        //topics
        TreeNode<Folder> topicsFolder = getFolder(TOPICS, () -> parentFolder.getValue().path());

        //topics/TOPIC_X_FOLDER  [...]
        addTopicXFolder(topicsFolder);

        //parentFolder/topics
        parentFolder.getChildren().add(topicsFolder);
    }

    private void addTopicXFolder(TreeNode<Folder> topicsFolder)
    {
        //TOPIC_X_FOLDER
        topicXFolder = getFolder(TOPIC_DIRECTORY_PLACE_HOLDER, () -> topicsFolder.getValue().path());

        //<TOPIC_X_FOLDER>/text
        addTextFolder(topicXFolder);

        //<TOPIC_X_FOLDER>/images
        addImagesFolder(topicXFolder);

        //<TOPIC_X_FOLDER>/questions [...]
        addQuestionsFolder(topicXFolder);

        topicsFolder.getChildren().add(topicXFolder);

    }

    private void addQuestionsFolder(TreeNode<Folder> topicXFolder)
    {
        //questions
        TreeNode<Folder> questionsFolder = getFolder(QUESTIONS, () -> topicXFolder.getValue().path());

        //questions/QUESTION_X_FOLDER [...]
        addQuestionXFolder(questionsFolder);

        //TOPIC_X_FOLDER/questions
        topicXFolder.getChildren().add(questionsFolder);
    }

    private void addQuestionXFolder(TreeNode<Folder> questionsFolder)
    {
        //QUESTION_X_FOLDER
        questionXFolder = getFolder(QUESTION_DIRECTORY_PLACE_HOLDER, () -> questionsFolder.getValue().path());

        //<QUESTION_X_FOLDER>/text
        addTextFolder(questionXFolder);

        //<QUESTION_X_FOLDER>/images
        addImagesFolder(questionXFolder);

        //<QUESTION_X_FOLDER>/answers [...]
        addAnswersFolder(questionXFolder);

        //questions/<ANSWERS_X_FOLDER
        questionsFolder.getChildren().add(questionXFolder);
    }


    private void addAnswersFolder(TreeNode<Folder> questionXFolder)
    {
        //answers
        TreeNode<Folder> answersFolder = getFolder(ANSWERS, () -> questionXFolder.getValue().path());

        //answers/ANSWER_X_FOLDER [...]
        addAnswerXFolder(answersFolder);

        //TOPIC_X_FOLDER/answers
        questionXFolder.getChildren().add(answersFolder);
    }

    private void addAnswerXFolder(TreeNode<Folder> answersFolder)
    {
        //ANSWER_X_FOLDER
        answerXFolder = getFolder(ANSWER_DIRECTORY_PLACE_HOLDER, () -> answersFolder.getValue().path());

        //<ANSWER_X_FOLDER>/text
        addTextFolder(answerXFolder);

        //<ANSWER_X_FOLDER>/images
        addImagesFolder(answerXFolder);

        //answers/<ANSWER_X_FOLDER
        answersFolder.getChildren().add(answerXFolder);
    }

    private void addTextFolder(TreeNode<Folder> parentFolder)
    {
        //parentFolder/text
        parentFolder.getChildren().add(getFolder(TEXT, () -> parentFolder.getValue().path()));
    }


    private TreeNode<Folder> getFolder(String name, Supplier<String> parentPath)
    {
        return new TreeNodeImp<>(getFolderImp(false, name, parentPath));
    }


    /**
     * Builds the relative file path of the units directory.
     *
     * @return the full relative file path of the units directory.
     */
    public synchronized String getUnitsDirectory()
    {
        return buildPath(unitsFolder, null);
    }

    private String buildPath(TreeNode<Folder> folderNode, Collection<TreeNode<Folder>> placeHolderNodes)
    {
        String sb = folderNode.getValue().path();
        if (placeHolderNodes != null) placeHolderNodes.forEach(phn -> phn.getValue().reset());
        return sb;
    }

    private ArrayList<Folder> climbTree(TreeNode<Folder> node)
    {
        ArrayList<Folder> hierarchy = new ArrayList<>();
        TreeNode<Folder> currentFolderNode = node;
        do
        {
            hierarchy.add(0, currentFolderNode.getValue());
            currentFolderNode = currentFolderNode.getParent();
        } while (currentFolderNode != null);
        return hierarchy;
    }


    /**
     * Builds the relative file path of the unit directory.
     *
     * @param unitDirectoryName the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @return the full relative file path of the unit directory.
     */
    public synchronized String getUnitDirectory(String unitDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        return buildPath(unitXFolder, Collections.singletonList(unitXFolder));
    }

    /**
     * Builds the relative file path of a unit's images directory.
     *
     * @param unitDirectoryName the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @return the full relative file path of the unit's images directory.
     */
    public synchronized String getUnitImagesDirectory(String unitDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        return buildPath(getChildFolderNode(unitXFolder, IMAGES), Collections.singletonList(unitXFolder));
    }

    private TreeNode<Folder> getChildFolderNode(TreeNode<Folder> unitXFolder, String images)
    {
        return asExp(unitXFolder.getChildren()).firstOrDefault(n -> n.getValue().name().equals(images));
    }

    private <T> Explorable<T> asExp(Collection<T> children)
    {
        return new net.raphjava.raphtility.collectionmanipulation.ArrayList<>(children);
    }

    /**
     * Builds the relative file path of a unit's text directory.
     *
     * @param unitDirectoryName the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @return the full relative file path of the unit's text directory.
     */
    public synchronized String getUnitTextDirectory(String unitDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        return buildPath(getChildFolderNode(unitXFolder, TEXT), Collections.singletonList(unitXFolder));
    }

    /**
     * Builds the relative file path of a unit's topics directory.
     *
     * @param unitDirectoryName the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @return the full relative file path of the unit's topics directory.
     */
    public synchronized String getUnitTopicsDirectory(String unitDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        return buildPath(getChildFolderNode(unitXFolder, TOPICS), Collections.singletonList(unitXFolder));
    }

    /**
     * Builds the relative file path of a topic's directory.
     *
     * @param unitDirectoryName  the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @return the full relative file path of the topic's directory.
     */
    public synchronized String getTopicDirectory(String unitDirectoryName, String topicDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        return buildPath(topicXFolder, Arrays.asList(unitXFolder, topicXFolder));
    }

    /**
     * Builds the relative file path of a topic's text directory.
     *
     * @param unitDirectoryName  the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @return the full relative file path of the topic's text directory.
     */
    public synchronized String getTopicTextDirectory(String unitDirectoryName, String topicDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        return buildPath(getChildFolderNode(topicXFolder, TEXT), Arrays.asList(unitXFolder, topicXFolder));
    }

    /**
     * Builds the relative file path of a topic's images directory.
     *
     * @param unitDirectoryName  the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @return the full relative file path of the topic's images directory.
     */
    public synchronized String getTopicImagesDirectory(String unitDirectoryName, String topicDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        return buildPath(getChildFolderNode(topicXFolder, IMAGES), Arrays.asList(unitXFolder, topicXFolder));
    }

    /**
     * Builds the relative file path of a topic's questions directory.
     *
     * @param unitDirectoryName  the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @return the full relative file path of the topic's questions directory.
     */
    public synchronized String getTopicQuestionsDirectory(String unitDirectoryName, String topicDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        return buildPath(getChildFolderNode(topicXFolder, QUESTIONS), Arrays.asList(unitXFolder, topicXFolder));
    }

    /**
     * Builds the relative file path of a question's directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @return the full relative file path of the question's directory.
     */
    public synchronized String getQuestionDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        return buildPath(questionXFolder, Arrays.asList(unitXFolder, topicXFolder, questionXFolder));
    }

    /**
     * Builds the relative file path of a question's text directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @return the full relative file path of the question's text directory.
     */
    public synchronized String getQuestionTextDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        return buildPath(getChildFolderNode(questionXFolder, TEXT), Arrays.asList(unitXFolder, topicXFolder, questionXFolder));


    }

    /**
     * Builds the relative file path of a question's images directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @return the full relative file path of the question's images directory.
     */
    public synchronized String getQuestionImagesDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        return buildPath(getChildFolderNode(questionXFolder, IMAGES), Arrays.asList(unitXFolder, topicXFolder, questionXFolder));


    }

    /**
     * Builds the relative file path of a question's answers directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @return the full relative file path of the question's answers directory.
     */
    public synchronized String getQuestionAnswersDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        return buildPath(getChildFolderNode(questionXFolder, ANSWERS), Arrays.asList(unitXFolder, topicXFolder, questionXFolder));


    }

    /**
     * Builds the relative file path of an answer's directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @param answerDirectoryName   the folder name of answer, not the full path, just the name e.g. answer_1, not question/answer_1.
     * @return the full relative file path of the answer's directory.
     */
    public synchronized String getAnswerDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName, String answerDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        answerXFolder.getValue().setName(answerDirectoryName);
        return buildPath(questionXFolder, Arrays.asList(unitXFolder, topicXFolder, questionXFolder, answerXFolder));
    }


    /**
     * Builds the relative file path of an answer's text directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @param answerDirectoryName   the folder name of answer, not the full path, just the name e.g. answer_1, not question/answer_1.
     * @return the full relative file path of the answer's text directory.
     */
    public synchronized String getAnswerTextDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName, String answerDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        answerXFolder.getValue().setName(answerDirectoryName);
        return buildPath(getChildFolderNode(answerXFolder, TEXT), Arrays.asList(unitXFolder, topicXFolder, questionXFolder, answerXFolder));
    }

    /**
     * Builds the relative file path of an answer's images directory.
     *
     * @param unitDirectoryName     the folder name of unit, not the full path, just the name e.g. math, not units/math.
     * @param topicDirectoryName    the folder name of topic, not the full path, just the name e.g. intro, not unit/intro.
     * @param questionDirectoryName the folder name of question, not the full path, just the name e.g. question_1, not topic/question_1.
     * @param answerDirectoryName   the folder name of answer, not the full path, just the name e.g. answer_1, not question/answer_1.
     * @return the full relative file path of the answer's images directory.
     */
    public synchronized String getAnswerImagesDirectory(String unitDirectoryName, String topicDirectoryName, String questionDirectoryName, String answerDirectoryName)
    {
        unitXFolder.getValue().setName(unitDirectoryName);
        topicXFolder.getValue().setName(topicDirectoryName);
        questionXFolder.getValue().setName(questionDirectoryName);
        answerXFolder.getValue().setName(answerDirectoryName);
        return buildPath(getChildFolderNode(answerXFolder, IMAGES), Arrays.asList(unitXFolder, topicXFolder, questionXFolder, answerXFolder));
    }

    private synchronized String getPath(Entity entity, String subDirectory)
    {
        LinkedList<Map.Entry<String, String>> b = pathMap.get(entity);
        if (b == null)
            throw new RuntimeException("Failed to find directory associated to " + entity + " and " + subDirectory);
        for (Map.Entry<String, String> e : b)
        {
            if (e.getKey().equals(subDirectory)) return e.getValue();
        }

        throw new RuntimeException("Failed to find directory associated to " + entity + " and " + subDirectory);
    }


    public enum Entity
    {
        TOPIC, QUESTION, ANSWER, UNIT
    }

    public static final class Builder
    {
        private String root;

        public Builder()
        {

        }

        public Builder root(String root)
        {
            this.root = root;
            return this;
        }

        public StudeeFileBuilder build()
        {
            StudeeFileBuilder fb = new StudeeFileBuilder(this);
//            fileManager.loadEntityFilePaths();
            fb.loadFileTree();
            return fb;
        }
    }
}
