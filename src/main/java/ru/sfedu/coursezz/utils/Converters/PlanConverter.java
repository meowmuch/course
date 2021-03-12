package ru.sfedu.coursezz.utils.Converters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.coursezz.models.Book;
import ru.sfedu.coursezz.models.Plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlanConverter extends AbstractBeanField {

    private String fieldsDelimiter = "=";
    private String elemDelimiter = "_";


    @Override
    protected Object convert(String value) {
        List<Plan> planList = new ArrayList<>();
        List<String> stringList = Arrays.asList(value.split(elemDelimiter));
        stringList.stream().forEach(el -> {
            Plan plan = new Plan();
            String[] parsedData = el.split(fieldsDelimiter);
            plan.setId(Long.parseLong(parsedData[0]));
            plan.setName(parsedData[1]);
            plan.setStartDay(String.valueOf(parsedData[2]));
            plan.setLastDay(String.valueOf(parsedData[3]));
            plan.setStatus(Boolean.valueOf(parsedData[4]));
            planList.add(plan);
        });
        return planList;
    }

    @Override
    public String convertToWrite(Object value) {
        List<Plan> planList = (List<Plan>) value;
        List<String> stringList = planList
                .stream()
                .map(el -> String.format("%d" + fieldsDelimiter +
                                         "%s" + fieldsDelimiter +
                                         "%s" + fieldsDelimiter +
                                         "%s" + fieldsDelimiter +
                                         "%b",
                                         el.getId(),
                                         el.getName(),
                                         el.getStartDay(),
                                         el.getLastDay(),
                                         el.getStatus()))
                                        .collect(Collectors.toList()
                                        );
        return String.join(elemDelimiter, stringList);
    }
}
