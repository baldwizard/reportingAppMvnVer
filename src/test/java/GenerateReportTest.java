import logic.EntityObject;
import logic.GenerateReport;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created By Piwosz on 17.12.2017
 */
@RunWith(JUnit4.class)
public class GenerateReportTest {

    private final static ArrayList<EntityObject> testData = new ArrayList<>();
    private final static TreeMap<String,Double> entityNamesPrices = new TreeMap<>();

    private final static TreeMap<LocalDate,Double> Outgoing = new TreeMap<>();
    private final static TreeMap<LocalDate,Double> Incoming = new TreeMap<>();

    private final static ArrayList<EntityObject> rankingAmountOfBuy = new ArrayList<>();
    private final static ArrayList<EntityObject> rankingAmountOfSell = new ArrayList<>();

    private static GenerateReport generateReport;

    @BeforeClass
    public static void init()
    {

        entityNamesPrices.put("foo",100.25);
        entityNamesPrices.put("bar",150.5);
        entityNamesPrices.put("foo2",60.2);
        entityNamesPrices.put("bar2",120.8);

        for(int i=0;i<100;i++)
        {
            testData.add(createRandomEntity());
        }

        //System.out.println("-----------------------");
        calcAggWithStandardFor();
        //System.out.println("-----------------------");

    }

    @Before
    public void initSomeOtherStuff()
    {
        generateReport = new GenerateReport(testData);
    }

    @Test
    public void checkOutgoingTotalsByDate()
    {
        Assert.assertEquals(Outgoing,generateReport.getAmountOfBuyByDate());

    }

    @Test
    public void checkIncomingTotalsByDate()
    {
        Assert.assertEquals(Incoming,generateReport.getAmountOfSellByDate());
    }

    @Test
    public void checkOutgoingInstructionRanking()
    {
        Assert.assertEquals(rankingAmountOfBuy,generateReport.getRankingEntitiesByHighestInstructionAmountOfTradeForBuy());
    }

    @Test
    public void checkIncomingInstructionRanking()
    {
        Assert.assertEquals(rankingAmountOfSell,generateReport.getRankingEntitiesByHighestInstructionAmountOfTradeForSell());
    }

    @AfterClass
    public static void printOutReport()
    {
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

    private static void calcAggWithStandardFor()
    {
        Map<String,Double> amountOfSellByName = new TreeMap<>();
        Map<String,Double> amountOfBuyByName = new TreeMap<>();

        Map<LocalDate,Double> amountOfSellByDate = new TreeMap<>();
        Map<LocalDate,Double> amountOfBuyByDate = new TreeMap<>();

        HashSet<EntityObject> highestBuy = new HashSet<>();
        HashSet<EntityObject> highestSell = new HashSet<>();

        for(EntityObject e:testData)
        {
            e.moveSettlementToFirstPossibleDate();

            Double amountBuy = amountOfBuyByName.get(e.getName());
            Double amountSell = amountOfSellByName.get(e.getName());

            Double amountBuyByDate = amountOfBuyByDate.get(e.getSettlementDate());
            Double amountSellByDate = amountOfSellByDate.get(e.getSettlementDate());

            amountBuy = (amountBuy!=null)?amountBuy:0;
            amountSell = (amountSell!=null)?amountSell:0;

            amountBuyByDate = (amountBuyByDate!=null)?amountBuyByDate:0;
            amountSellByDate = (amountSellByDate!=null)?amountSellByDate:0;


            if(e.getEntityType()== EntityObject.EntityTypeEnum.BUY)
            {
                amountOfBuyByName.put(e.getName(),e.amountOfTrade()+amountBuy);
                amountOfBuyByDate.put(e.getSettlementDate(),e.amountOfTrade()+amountBuyByDate);

                if(!highestBuy.contains(e))
                    highestBuy.add(e);
                else
                {
                    boolean removed = highestBuy.removeIf(
                            entity ->
                                    entity.equals(e) &&
                                            entity.amountOfTrade() < e.amountOfTrade()
                    );

                    if(removed)
                        highestBuy.add(e);

                }
            }
            else
            {
                amountOfSellByName.put(e.getName(),e.amountOfTrade()+amountSell);
                amountOfSellByDate.put(e.getSettlementDate(),e.amountOfTrade()+amountSellByDate);

                if(!highestSell.contains(e))
                        highestSell.add(e);
                else
                {
                    boolean removed = highestSell.removeIf(
                            entity ->
                                      entity.equals(e) &&
                                      entity.amountOfTrade() < e.amountOfTrade()
                    );

                  if(removed)
                     highestSell.add(e);

                }
            }

        }

        Comparator<EntityObject> amountCompare = (o1,o2)->(int)(o2.amountOfTrade()-o1.amountOfTrade());

        rankingAmountOfBuy.addAll(highestBuy);
        rankingAmountOfSell.addAll(highestSell);

        rankingAmountOfBuy.sort(amountCompare);
        rankingAmountOfSell.sort(amountCompare);

        Outgoing.putAll(amountOfBuyByDate);
        Incoming.putAll(amountOfSellByDate);
    }

    private void printAggregates()
    {
        System.out.println("Highest Amount of Buy (Outgoing in USD by Entity name):\n\t");

        for(int i=0;i<rankingAmountOfBuy.size();i++)
        {
            System.out.println("["+(i+1)+"]"+rankingAmountOfBuy.get(i).toString());
        }

        System.out.println("Highest Amount of Sell (Incoming in USD by Entity name):\n\t");

        for(int i=0;i<rankingAmountOfSell.size();i++)
        {
            System.out.println("["+(i+1)+"]"+rankingAmountOfSell.get(i).toString());
        }

        System.out.println("Aggregated Amount of Buy by Date (Outgoing in USD by Date):\n\t"+Outgoing.toString().replace(",","\n"));
        System.out.println("Aggregated Amount of Sell by Date (Incoming in USD by Date):\n\t"+Incoming.toString().replace(",","\n"));
    }

}
