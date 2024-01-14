package ru.msvdev.homefinance.task.statistic.category;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import ru.msvdev.homefinance.controller.utility.statistic.simple.property.FilterPropertyContainer;
import ru.msvdev.homefinance.data.entity.ExpenseEntity;
import ru.msvdev.homefinance.utils.DeDuplicator;
import ru.msvdev.homefinance.utils.HashMapDeDuplicator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryStatisticProcessor {

    public static final String ALL_CATEGORIES = "Все";

    @Getter
    private List<String> categories;

    private Map<String, List<Expense>> expenseMap;

    @Getter
    private LocalDate minDate;
    @Getter
    private LocalDate maxDate;


    public List<CategoryStatisticItem> select(String category, LocalDate fromDate, LocalDate toDate, FilterPropertyContainer.Period period) {
        if (toDate.isBefore(fromDate)) return new ArrayList<>();

        Collection<List<Expense>> expensesLists;
        if (category.equalsIgnoreCase(ALL_CATEGORIES)) {
            expensesLists = expenseMap.values();

        } else if (expenseMap.containsKey(category)) {
            expensesLists = new ArrayList<>();
            expensesLists.add(expenseMap.get(category));

        } else {
            return new ArrayList<>();
        }

        switch (period) {
            case YEAR:
                return yearPeriodSelection(expensesLists, fromDate, toDate);
            case MONTH:
                return monthPeriodSelection(expensesLists, fromDate, toDate);
        }

        return new ArrayList<>();
    }

    private List<CategoryStatisticItem> yearPeriodSelection(Collection<List<Expense>> expensesLists, LocalDate fromDate, LocalDate toDate) {
        int fromYear = fromDate.getYear();
        int toYear = toDate.getYear();
        int countOfYears = toYear - fromYear + 1;

        BigDecimal[] bars = new BigDecimal[countOfYears];
        Arrays.fill(bars, BigDecimal.ZERO);

        for (List<Expense> expenses : expensesLists) {
            for (Expense expense : expenses) {
                LocalDate date = expense.date;
                if (date.isBefore(fromDate) || date.isAfter(toDate)) continue;

                int index = date.getYear() - fromYear;
                bars[index] = bars[index].add(expense.cost);
            }
        }

        List<CategoryStatisticItem> categoryStatisticItems = new ArrayList<>();

        for (int i = countOfYears - 1; i >= 0; i--) {
            String period = String.format("%d г.", fromYear + i);
            String cost = String.format("%,.2f ₽", bars[i]);

            CategoryStatisticItem item = new CategoryStatisticItem(
                    new SimpleStringProperty(period),
                    new SimpleStringProperty(cost)
            );

            categoryStatisticItems.add(item);
        }

        return categoryStatisticItems;
    }

    private final String[] monthNames = {
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    };

    private List<CategoryStatisticItem> monthPeriodSelection(Collection<List<Expense>> expensesLists, LocalDate fromDate, LocalDate toDate) {
        int fromYear = fromDate.getYear();
        int fromMonth = fromDate.getMonthValue();
        int toYear = toDate.getYear();
        int toMonth = toDate.getMonthValue();

        int countOfMonths = (toYear - fromYear) * 12 + toMonth - fromMonth + 1;
        int offsetIndex = fromYear * 12 + fromMonth;

        BigDecimal[] bars = new BigDecimal[countOfMonths];
        Arrays.fill(bars, BigDecimal.ZERO);

        for (List<Expense> expenses : expensesLists) {
            for (Expense expense : expenses) {
                LocalDate date = expense.date;
                if (date.isBefore(fromDate) || date.isAfter(toDate)) continue;

                int index = (date.getYear() - fromYear) * 12 + date.getMonthValue() - fromMonth;
                bars[index] = bars[index].add(expense.cost);
            }
        }

        List<CategoryStatisticItem> categoryStatisticItems = new ArrayList<>();

        for (int i = countOfMonths - 1; i >= 0; i--) {
            int year = (i + offsetIndex - 1) / 12;
            int month = (i + offsetIndex - 1) % 12;

            String period = String.format("%d г. %s", year, monthNames[month]);
            String cost = String.format("%,.2f ₽", bars[i]);

            CategoryStatisticItem item = new CategoryStatisticItem(
                    new SimpleStringProperty(period),
                    new SimpleStringProperty(cost)
            );

            categoryStatisticItems.add(item);
        }

        return categoryStatisticItems;
    }

    /**
     * Подготовить информацию для организации выборок данных
     *
     * @param expenseEntities коллекция записей о расходах
     */
    public void prepare(List<ExpenseEntity> expenseEntities) {
        expenseMap = new HashMap<>();
        minDate = LocalDate.now();
        maxDate = LocalDate.now();

        DeDuplicator<String> stringDeDuplicator = new HashMapDeDuplicator<>();
        DeDuplicator<LocalDate> localDateDeDuplicator = new HashMapDeDuplicator<>();
        DeDuplicator<BigDecimal> costDeDuplicator = new HashMapDeDuplicator<>();

        Set<String> categories = new HashSet<>();

        for (ExpenseEntity entity : expenseEntities) {
            String category = stringDeDuplicator.deDuplicate(entity.getCategory().getName());
            LocalDate date = localDateDeDuplicator.deDuplicate(entity.getDate());
            BigDecimal cost = costDeDuplicator.deDuplicate(entity.getCost());

            categories.add(category);

            if (!expenseMap.containsKey(category)) expenseMap.put(category, new ArrayList<>());
            expenseMap.get(category).add(new Expense(date, cost));

            if (minDate.isAfter(date)) minDate = date;
            if (maxDate.isBefore(date)) maxDate = date;
        }

        this.categories = categories.stream().sorted().collect(Collectors.toList());
    }


    private class Expense {
        public final LocalDate date;
        public final BigDecimal cost;

        public Expense(LocalDate date, BigDecimal cost) {
            this.date = date;
            this.cost = cost;
        }
    }

}
