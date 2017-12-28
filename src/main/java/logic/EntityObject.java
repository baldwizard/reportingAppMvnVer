package logic;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created By Piwosz on 15.12.2017
 */

public class EntityObject implements Instruction,Comparable<EntityObject> {

    public enum EntityTypeEnum {
        BUY,
        SELL
    }

    public enum CurrencyTypeEnum
    {
        USD,
        PLN,
        SGP,
        AED,
        SAR
    }

    private String name;
    private EntityTypeEnum entityType;

    private double agreedFx;
    private CurrencyTypeEnum currencyType;

    private java.time.LocalDate instructionDate;
    private java.time.LocalDate settlementDate;

    private double pricePerUnit;
    private int units;


    public EntityObject(){
        this.currencyType = CurrencyTypeEnum.USD;
        this.entityType = EntityTypeEnum.BUY;
        this.settlementDate = LocalDate.of(2017,01,01);
    }

    public EntityObject(String entityName,
                        EntityTypeEnum entityTypeEnum,
                        double agreedFx,
                        CurrencyTypeEnum currencyType,
                        LocalDate instructionDate,
                        LocalDate settlementDate,
                        int units,
                        double pricePerUnit
    )
    {
        this.name = entityName;
        this.entityType = entityTypeEnum;
        this.agreedFx = agreedFx;
        this.currencyType = currencyType;
        this.instructionDate = instructionDate;
        this.settlementDate = settlementDate;
        this.units = units;
        this.pricePerUnit = pricePerUnit;
    }


    @Override
    public double amountOfTrade() {
        return pricePerUnit * units * agreedFx;
    }

    @Override
    public void moveSettlementToFirstPossibleDate() {

        switch(currencyType)
        {
            case AED:
            case SAR:
                settlementDate = moveDate(this.settlementDate, false);
                break;
            default:
                settlementDate = moveDate(this.settlementDate, true);
                break;
        }
    }

    private LocalDate moveDate(LocalDate settlementDate,boolean endOfTheWeekOnFriday)
    {
        LocalDate newSettlementDate = settlementDate;
        DayOfWeek dayOfWeek = settlementDate.getDayOfWeek();

        int offset = 0;

        if(endOfTheWeekOnFriday)
        {
            if(dayOfWeek == DayOfWeek.SATURDAY)
                offset = 2;
            else if(dayOfWeek == DayOfWeek.SUNDAY)
                offset = 1;
        }
        else
        {
            if(dayOfWeek == DayOfWeek.FRIDAY)
                offset = 2;
            else if(dayOfWeek == DayOfWeek.SATURDAY)
                offset = 1;
        }

        return newSettlementDate.plusDays(offset);
    }

    public String getName() {
        return name;
    }

    public EntityTypeEnum getEntityType() {
        return entityType;
    }

    public CurrencyTypeEnum getCurrencyType() {
        return currencyType;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    @Override
    public int compareTo(EntityObject o) {

        double leftOrRight = this.amountOfTrade() - o.amountOfTrade();

        if(leftOrRight > 0) return 1;
        else if(leftOrRight < 0 ) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityObject entityObject = (EntityObject) o;

        if (!name.equals(entityObject.name)) return false;
        return entityType == entityObject.entityType;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + entityType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", entityType=" + entityType +
                ", agreedFx=" + agreedFx +
                ", currencyType=" + currencyType +
                ", instructionDate=" + instructionDate +
                ", settlementDate=" + settlementDate +
                ", pricePerUnit=" + pricePerUnit +
                ", units=" + units +
                ", amountOfTrade="+amountOfTrade() +
                '}';
    }
}
