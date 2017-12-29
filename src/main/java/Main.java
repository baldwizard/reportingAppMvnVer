import logic.EntityObject;
import logic.GenerateReport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created By Piwosz on 29.12.2017
 */

public class Main {

    private static TreeMap<String,Double> entityNamesPrices;
    private static ArrayList<EntityObject> entityObjectArrayList;
    private static int maxEntityObjects = 10;

    public static void main(String[] args) {

        GenerateReport generateReport;

        entityNamesPrices = new TreeMap<>();
        entityObjectArrayList = new ArrayList<>();

        entityNamesPrices.put("foo", 100.25);
        entityNamesPrices.put("bar", 150.5);
        entityNamesPrices.put("foo2", 60.2);
        entityNamesPrices.put("bar2", 120.8);


        System.out.println("------Input Data-----");
        for (int i = 0; i < maxEntityObjects; i++)
        {
            EntityObject entityObject = createRandomEntity();
            entityObjectArrayList.add(entityObject);

            System.out.println(entityObject);
        }
        System.out.println("---------------------");

        generateReport = new GenerateReport(entityObjectArrayList);

        generateReport.printReport();

    }


    private static EntityObject createRandomEntity()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        String name = (String)entityNamesPrices.keySet().toArray()[(random.nextInt(entityNamesPrices.size()))];

        LocalDate instructionDate = LocalDate.of(2017,12,random.nextInt(30)+1);

        return new EntityObject(name,
                EntityObject.EntityTypeEnum.values()[random.nextInt(EntityObject.EntityTypeEnum.values().length)],
                random.nextDouble(0.5),
                EntityObject.CurrencyTypeEnum.values()[random.nextInt(EntityObject.CurrencyTypeEnum.values().length)],
                instructionDate,
                instructionDate.plusDays(1),
                random.nextInt(500)+1,
                entityNamesPrices.get(name)
        );
    }
}
