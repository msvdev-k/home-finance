package ru.msvdev.homefinance.io.xhb;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XmlHBParser {

    private static final Long DAYS_TO_EPOCH = 719163L;

    private static final String HOME_BANK = "homebank";

    private static final String CATEGORY = "cat";
    private static final String CATEGORY_KEY = "key";
    private static final String CATEGORY_NAME = "name";
    private static final String CATEGORY_PARENT = "parent";

    private static final String OPERATION = "ope";
    private static final String OPERATION_DATE = "date";
    private static final String OPERATION_AMOUNT = "amount";
    private static final String OPERATION_CATEGORY = "category";
    private static final String OPERATION_WORDING = "wording";


    private final Map<Integer, Category> categoryMap = new HashMap<>();
    private final List<Operation> operations = new ArrayList<>();


    public Map<Integer, Category> getCategoryMap() {
        return categoryMap;
    }

    public List<Operation> getOperations() {
        return operations;
    }


    public void parseFile(Path path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(path.toFile());

        parseDocument(doc);
    }

    public void parseDocument(Document doc) {
        Element root = doc.getDocumentElement();
        if (!root.getTagName().equals(HOME_BANK)) {
            throw new RuntimeException("Коренной элемент документа должен называться " + HOME_BANK);
        }

        parseCategories(root);
        parseOperations(root);
    }


    private void parseCategories(Element root) {
        NodeList nodes = root.getElementsByTagName(CATEGORY);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                NamedNodeMap attributes = element.getAttributes();

                String id = attributes.getNamedItem(CATEGORY_KEY).getNodeValue();
                String name = attributes.getNamedItem(CATEGORY_NAME).getNodeValue();
                Node parentNode = attributes.getNamedItem(CATEGORY_PARENT);
                String parentId = parentNode != null ? parentNode.getNodeValue() : null;

                Category category = new Category();
                category.setName(name);
                category.setParent(parentId);

                categoryMap.put(Integer.parseInt(id), category);
            }
        }

        for (Category category : categoryMap.values()) {
            String parent = category.getParent();
            if (parent != null) {
                category.setParent(categoryMap.get(Integer.parseInt(parent)).getName());
            }
        }
    }

    private void parseOperations(Element root) {
        NodeList nodes = root.getElementsByTagName(OPERATION);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                NamedNodeMap attributes = element.getAttributes();

                String dateString = attributes.getNamedItem(OPERATION_DATE).getNodeValue();
                String amountString = attributes.getNamedItem(OPERATION_AMOUNT).getNodeValue();
                String categoryString = attributes.getNamedItem(OPERATION_CATEGORY).getNodeValue();
                Node noteNode = attributes.getNamedItem(OPERATION_WORDING);
                String noteString = noteNode != null ? noteNode.getNodeValue() : null;

                LocalDate date = LocalDate.ofEpochDay(Long.parseLong(dateString) - DAYS_TO_EPOCH);
                double amountDouble = Double.parseDouble(amountString);
                if (amountDouble >= 0) continue; // Добавляются только записи расходов (записи доходов не добавляются)
                BigDecimal amount = BigDecimal
                        .valueOf(-amountDouble)
                        .setScale(2, RoundingMode.HALF_UP);
                Integer categoryId = Integer.parseInt(categoryString);

                Operation operation = new Operation();
                operation.setDate(date);
                operation.setAmount(amount);
                operation.setCategory(categoryId);
                operation.setNote(noteString);

                operations.add(operation);
            }
        }
    }
}
