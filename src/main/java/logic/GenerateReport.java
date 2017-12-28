package logic;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By Piwosz on 15.12.2017
 */

public class GenerateReport {

    private ArrayList<EntityObject> entities;

    private Map<LocalDate,Double> amountOfSellByDate;
    private Map<LocalDate,Double> amountOfBuyByDate;

    private Map<String,Double> amountOfSellByName;
    private Map<String,Double> amountOfBuyByName;

    private List<EntityObject> rankingEntitiesByHighestInstructionAmountOfTradeForBuy;

    private List<EntityObject> rankingEntitiesByHighestInstructionAmountOfTradeForSell;

    public GenerateReport(ArrayList<EntityObject> collections)
    {
        this.entities = collections;
        generateStatistics();
    }

    private void generateStatistics()
    {
        amountOfBuyByName = entities.stream()
                                .filter(entityObject -> entityObject.getEntityType()== EntityObject.EntityTypeEnum.BUY)
                                //.collect(Collectors.groupingBy(e->e.getName(),Collectors.summingDouble(e->e.amountOfTrade())));
                                .collect(Collectors.toMap(e->e.getName(),e->e.amountOfTrade(),(Double o2,Double o1)->o2+o1,TreeMap::new));


        amountOfSellByName = entities.stream()
                                .filter(entityObject -> entityObject.getEntityType()== EntityObject.EntityTypeEnum.SELL)
                                //.collect(Collectors.groupingBy(e->e.getName(), Collectors.summingDouble(e->e.amountOfTrade())));
                                .collect(Collectors.toMap(e->e.getName(),e->e.amountOfTrade(),(Double o2,Double o1)->o2+o1,TreeMap::new));

        amountOfBuyByDate = entities.stream()
                               .filter(entityObject -> entityObject.getEntityType()== EntityObject.EntityTypeEnum.BUY)
                                .map(e->{
                                    e.moveSettlementToFirstPossibleDate();
                                    return e;
                                })
                                //.collect(Collectors.groupingBy(e->e.getSettlementDate(), Collectors.summingDouble(e->e.amountOfTrade())));
                                 .collect(Collectors.toMap(e->e.getSettlementDate(), e->e.amountOfTrade(),(o1,o2)->o1+o2,TreeMap::new));

        amountOfSellByDate = entities.stream()
                    .filter(entityObject -> entityObject.getEntityType()== EntityObject.EntityTypeEnum.SELL)
                    .map(e->{
                        e.moveSettlementToFirstPossibleDate();
                        return e;
                    })
                    //.collect(Collectors.groupingBy(e->e.getSettlementDate(), Collectors.summingDouble(e->e.amountOfTrade())));
                    .collect(Collectors.toMap(e->e.getSettlementDate(), e->e.amountOfTrade(),(o1,o2)->o1+o2,TreeMap::new));


        rankingEntitiesByHighestInstructionAmountOfTradeForBuy =entities.stream()
                .filter(entityObject -> entityObject.getEntityType() == EntityObject.EntityTypeEnum.BUY)
                .sorted(Collections.reverseOrder())
                .distinct()
                .collect(Collectors.toList());

         rankingEntitiesByHighestInstructionAmountOfTradeForSell =entities.stream()
                .filter(entityObject -> entityObject.getEntityType() == EntityObject.EntityTypeEnum.SELL)
                .sorted(Collections.reverseOrder())
                .distinct()
                .collect(Collectors.toList());


    }

    public Map<LocalDate, Double> getAmountOfSellByDate() {
        return amountOfSellByDate;
    }

    public Map<LocalDate, Double> getAmountOfBuyByDate() {
        return amountOfBuyByDate;
    }

    public Map<String, Double> getAmountOfSellByName() {
        return amountOfSellByName;
    }

    public Map<String, Double> getAmountOfBuyByName() {
        return amountOfBuyByName;
    }

    public List<EntityObject> getRankingEntitiesByHighestInstructionAmountOfTradeForBuy() {
        return rankingEntitiesByHighestInstructionAmountOfTradeForBuy;
    }

    public List<EntityObject> getRankingEntitiesByHighestInstructionAmountOfTradeForSell() {
        return rankingEntitiesByHighestInstructionAmountOfTradeForSell;
    }

    public void printReport()
    {
        System.out.println(
                "---------------------------\n"+
                "Amount Outgoing (Buy) By Date\n"+
                "---------------------------\n");
        amountOfSellByDate.forEach((k,v)->System.out.println("date:"+k+" value"+v));
        System.out.println(
                "\n----------------------------\n"+
                "Amount Incoming (Sell) By Date\n"+
                "----------------------------\n");
        amountOfBuyByDate.forEach((k,v)->System.out.println("date:"+k+" value"+v));
        /*System.out.println(
                "\n---------------------------------\n"+
                "Ranking entities By Sell amount  \n"+
                "---------------------------------\n");
        amountOfSellByName.forEach((s,d)->System.out.println(" "+s+":"+d));
        System.out.println(
                "\n---------------------------------\n"+
                "Ranking entities By Buy amount  \n"+
                "---------------------------------\n");
        amountOfBuyByName.forEach((s,d)->System.out.println(" "+s+":"+d));
        */
        System.out.println(
                "\n---------------------------------\n"+
                "Ranking entities By Highest Instruction Buy amount  \n"+
                "---------------------------------\n");
        rankingEntitiesByHighestInstructionAmountOfTradeForBuy.forEach((e)->System.out.println(" "+e.getName()+":"+e.amountOfTrade()));
        System.out.println(
                "\n---------------------------------\n"+
                "Ranking entities By Highest Instruction Sell amount  \n"+
                "---------------------------------\n");
        rankingEntitiesByHighestInstructionAmountOfTradeForSell.forEach((e)->System.out.println(" "+e.getName()+":"+e.amountOfTrade()));

    }

}
