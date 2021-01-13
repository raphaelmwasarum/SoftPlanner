package com.raphjava.softplanner.services;//package net.raphjava.studeeconsole.services;
//
//import data.interfaces.IOService;
//import net.raphjava.raphtility.collectionmanipulation.ArrayList;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.List;
//import net.raphjava.studeeconsole.components.AbFactoryBean;
//import net.raphjava.studeeconsole.components.AbstractFactory;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.Collection;
//
//import static net.raphjava.di.annotations.Scope.Singleton;
//
////@Lazy
///*@Scope(Singleton)*/
////@Component
//public class HTMLParser
//{
//
//
//    private IOService ioService;
//
//
//
//    /*public HTMLParser(IOService ioService, KeyGenerator keyGenerator)
//    {
//        this.keyGenerator = keyGenerator;
//        this.ioService = ioService;
//
//    }*/
//
//
//    private Document document;
//
//    private HTMLParser(Builder builder)
//    {
//        ioService = builder.ioService;
//        keyGenerator = builder.keyGenerator;
//    }
//
//    public static Builder newBuilder()
//    {
//        return new Builder();
//    }
//
//
//    public void setFile(String file)
//    {
//        this.file = file;
//    }
//
//    private String file;
//
//    /**
//     * Loads up a new HTML document with head and body tags loaded.
//     */
//    public void initialize()
//    {
//        var html = new StringBuilder();
//        loadElementVerbose("head", html);
//        loadElementVerbose("body", html);
//        loadRootHTML5(html);
//        parseHtml(html.toString(), true);
//    }
//
//
//    /** Loads up the passed html string.
//     * @param html
//     */
//    public void initialize(String html)
//    {
//        parseHtml(html, true);
//    }
//
//    public boolean commit()
//    {
//        assignIDToNewElements();
//        removeURLs();
//        return ioService.overWriteFile(file, document.toString());
//    }
//
//    private void removeURLs()
//    {
//        /*var imageElements = elementsAsExp().where(e -> e.tag().getName().equals("img")).list();
//        for(var i : imageElements)
//        {
//            i.attr("src", "");
//        }*/
//
//        for(var e : document.getAllElements())
//        {
//            if(e.tag().getName().equals("img")) removeImageUrl(e);
//            else if(e.tag().getName().equals("body")) replaceStyleUrlWithResourceName(e);
//        }
//    }
//
//    private String stylesSheetFileName;
//
//    public void setStylesSheetFileName(String stylesSheetFileName)
//    {
//        this.stylesSheetFileName = stylesSheetFileName;
//    }
//
//    private void replaceStyleUrlWithResourceName(Element element)
//    {
//        var styleElement = elementsAsExp(element.getAllElements()).firstOrDefault(e -> e.tag().getName().equals("link") && e.attr("rel").equals("stylesheet"));
//        if(styleElement == null) return;
//        styleElement.attr("href", stylesSheetFileName);
//    }
//
//    private void removeImageUrl(Element e)
//    {
//        e.attr("src", "");
//    }
//
//    private Explorable<String> usedIDs = new ArrayList<>();
//
//    private void assignIDToNewElements()
//    {
//        for(var element : document.getAllElements())
//        {
//            var id = element.id();
//            if(id.isBlank()) element.attr("id", getNewID());
//        }
//    }
//
//    private KeyGenerator keyGenerator;
//
//    private String getNewID()
//    {
//        String newID;
//        do
//        {
//            newID = "el" + String.valueOf(keyGenerator.getKey());
//        }
//        while (usedIDs.contains(newID));
//        usedIDs.add(newID);
//        return newID;
//    }
//
//    private void parseHtml(String html, boolean extractIDs)
//    {
//        document = Jsoup.parse(html);
//        resolveURLs();
//        if (extractIDs)
//        {
//            usedIDs.clear();
//            extractIDs();
//        }
//    }
//
//    private void resolveURLs()
//    {
//        /*var imageElements = elementsAsExp().where(e -> e.tag().getName().equals("img")).list();
//        for(var i : imageElements)
//        {
//            var alt = i.attr("alt");
//            if(alt == null || alt.isBlank()) continue;
//            var url = ioService.getImageURL(alt);
//            i.attr("src", "file:" + url);
//        }*/
//
//        for(var e : document.getAllElements())
//        {
//            if(e.tag().getName().equals("img")) resolveImageUrl(e);
//            else if(e.tag().getName().equals("body")) resolveStyleUrl(e);
//        }
//    }
//
//    private void resolveStyleUrl(Element element)
//    {
//        var styleElement = elementsAsExp(element.getAllElements()).firstOrDefault(e -> e.tag().getName().equals("link") && e.attr("rel").equals("stylesheet"));
//        var styleUrl = getStylesSheetURL();
//        if(styleElement == null)
//        {
//            styleElement = document.createElement("link");
////            styleElement.attr("<link rel=\"stylesheet\" href=\"" + styleUrl + "\">")
//            styleElement.attr("rel", "stylesheet");
//            styleElement.attr("href", "file:" + styleUrl);
//            element.insertChildren(0, styleElement);
//            return;
//        }
//        styleElement.attr("href", "file:" + styleUrl);
//    }
//
//    private String getStylesSheetURL()
//    {
//        return ioService.getStylesUrl("styles.css");
//    }
//
//    private Explorable<Element> elementsAsExp(Elements allElements)
//    {
//        return new ArrayList<>(allElements);
//    }
//
//    private void resolveImageUrl(Element i)
//    {
//        var alt = i.attr("alt");
//        if(alt == null || alt.isBlank()) return;
//        var url = ioService.getImageURL(alt);
//        i.attr("src", "file:" + url);
//    }
//
//    private void extractIDs()
//    {
//        for(var el : document.getAllElements())
//        {
//            var id = el.id();
//            if(!id.isBlank()) usedIDs.add(id);
//        }
//    }
//
//
//    private void loadElementVerbose(String elementName, StringBuilder html)
//    {
//        html.append(setStartTag(elementName)).append(wrapWithEndTag(elementName));
//    }
//
//    private String wrapWithEndTag(String elementName)
//    {
//        return "</" + elementName + ">";
//    }
//
//    private String setStartTag(String elementName)
//    {
//        return "<" + elementName + ">";
//    }
//
//    private void loadRootHTML5(StringBuilder html)
//    {
//        html.insert(0, "<!DOCTYPE html>");
//        html.append("</html>");
//    }
//
//    public String getHtml()
//    {
//        return document.toString();
//    }
//
//    public void setHeadText(String headText)
//    {
//        var h = document.getElementsByTag("head");
//        h.prepend(headText);
//    }
//
//    public void setHeading(String text, int headingLevel)
//    {
//        var b = document.getElementsByTag("body");
//        if(b == null || b.isEmpty()) throw new RuntimeException("HTML Error: Couldn't find the body element.");
//        b.get(0).html(wrapWithH(text, headingLevel));
//    }
//
//    private String wrapWithH(String text, int headingLevel)
//    {
//        return "<h" + headingLevel + ">" + text + "</h" + headingLevel + ">";
//    }
//
//    public void reload(String htmlText)
//    {
//        parseHtml(htmlText, false);
//    }
//
//    public void deleteContent()
//    {
//        //Delete all images
//        if(document == null) return;
//        for(var e : document.getAllElements())
//        {
//            if(e.tag().getName().equals("img"))
//            {
//                var imageFileName = e.attr("src").replace("file:", "");
//                ioService.deleteFile(new File(imageFileName));
//            }
//        }
//        //Delete html file
//        ioService.deleteFile(new File(file));
//    }
//
//    /** Builds a text string that concatenates the data in the elements of the ids passed.
//     * @param htmlIds ids of the elements whose data is used to build the result string.
//     * @return A string of the data from the elements whose ids have been passed.
//     */
//    public String buildText(List<String> htmlIds)
//    {
//        if(htmlIds.isEmpty()) return "";
//        var data = new StringBuilder();
//        var firstId = htmlIds.firstOrDefault();
//        var fEl = document.getElementById(firstId);
//        data.append(fEl.ownText());
//        for(var id : htmlIds)
//        {
//            if(id.equals(firstId)) continue;
//            var el = document.getElementById(id);
//            var d = el.ownText();
//            data.append(". ").append(d != null ? d : "");
//        }
//        return data.append(".").toString();
//    }
//
//    public boolean isInitialized()
//    {
//        return document != null;
//    }
//
//    public Explorable<Element> getHtmlNodes()
//    {
//        return elementsAsExp(document.getAllElements());
//    }
//
//
//    public final static String FACTORY = "htmlParser";
//
//
//    @Lazy
//    @Scope(Singleton)
//    @Component(FACTORY)
//    public static final class Builder extends AbFactoryBean<HTMLParser>
//    {
//        private IOService ioService;
//        private KeyGenerator keyGenerator;
//
//        private Builder()
//        {
//            super(HTMLParser.class);
//        }
//
//        @Autowired
//        public Builder ioService(IOService ioService)
//        {
//            this.ioService = ioService;
//            return this;
//        }
//
//        @Autowired
//        public Builder keyGenerator(KeyGenerator keyGenerator)
//        {
//            this.keyGenerator = keyGenerator;
//            return this;
//        }
//
//        public HTMLParser build()
//        {
//            return new HTMLParser(this);
//        }
//
//
//    }
//}
