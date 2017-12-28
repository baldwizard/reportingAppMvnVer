import logic.EntityObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Created By Piwosz on 15.12.2017
 */

@RunWith(JUnit4.class)
public class EntityObjectTest {


    @Test
    public void createTest()
    {
        EntityObject entityObject = new EntityObject();

        assertEquals(entityObject.amountOfTrade() , 0,0);
        assertEquals(entityObject.getCurrencyType(), EntityObject.CurrencyTypeEnum.USD);
        assertEquals(entityObject.getEntityType(), EntityObject.EntityTypeEnum.BUY);
        assertEquals(entityObject.getSettlementDate(), LocalDate.of(2017,01,01));
    }

    @Test
    public void equalsTest()
    {
        EntityObject entityObjectMondaySunday = new EntityObject("bar",
                EntityObject.EntityTypeEnum.BUY,
                0.5,
                EntityObject.CurrencyTypeEnum.SGP,
                LocalDate.of(2017,12,14),
                LocalDate.of(2017,12,15),
                100,
                10.25
        );

        EntityObject entityObjectSundayThursday = new EntityObject("bar",
                EntityObject.EntityTypeEnum.BUY,
                0.5,
                EntityObject.CurrencyTypeEnum.AED,
                LocalDate.of(2017,12,14),
                LocalDate.of(2017,12,15),
                100,
                10.25
        );

        assertEquals(true,entityObjectMondaySunday.equals(entityObjectSundayThursday));
    }


    @Test
    public void checkSettlementDateForFridayForBothWeekOptions()
    {

        EntityObject entityObjectMondaySunday = new EntityObject("foo",
                                        EntityObject.EntityTypeEnum.BUY,
                                        0.5,
                                        EntityObject.CurrencyTypeEnum.SGP,
                                        LocalDate.of(2017,12,14),
                                        LocalDate.of(2017,12,15),
                                        100,
                                        10.25
                                        );

        EntityObject entityObjectSundayThursday = new EntityObject("bar",
                                                        EntityObject.EntityTypeEnum.BUY,
                                                        0.5,
                                                        EntityObject.CurrencyTypeEnum.AED,
                                                        LocalDate.of(2017,12,14),
                                                        LocalDate.of(2017,12,15),
                                                        100,
                                                        10.25
                                                );


        LocalDate settlementDate = entityObjectMondaySunday.getSettlementDate();
        entityObjectMondaySunday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectMondaySunday.getSettlementDate().getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, entityObjectMondaySunday.getSettlementDate().getDayOfWeek());

        settlementDate = entityObjectSundayThursday.getSettlementDate();
        entityObjectSundayThursday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
        assertEquals( DayOfWeek.SUNDAY, entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
    }

    @Test
    public void checkSettlementDateForSaturdayForBothWeekOptions()
    {

        EntityObject entityObjectMondaySunday = new EntityObject("foo",
                                        EntityObject.EntityTypeEnum.BUY,
                                        0.5,
                                        EntityObject.CurrencyTypeEnum.SGP,
                                        LocalDate.of(2017,12,15),
                                        LocalDate.of(2017,12,16),
                                        100,
                                        10.25
                                        );

        EntityObject entityObjectSundayThursday = new EntityObject("bar",
                                                        EntityObject.EntityTypeEnum.BUY,
                                                        0.5,
                                                        EntityObject.CurrencyTypeEnum.AED,
                                                        LocalDate.of(2017,12,15),
                                                        LocalDate.of(2017,12,16),
                                                        100,
                                                        10.25
                                                );


        LocalDate settlementDate = entityObjectMondaySunday.getSettlementDate();
        entityObjectMondaySunday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectMondaySunday.getSettlementDate().getDayOfWeek());
        assertEquals(DayOfWeek.MONDAY, entityObjectMondaySunday.getSettlementDate().getDayOfWeek());

        settlementDate = entityObjectSundayThursday.getSettlementDate();
        entityObjectSundayThursday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
    }

    @Test
    public void checkSettlementDateForSundayForBothWeekOptions()
    {

        EntityObject entityObjectMondaySunday = new EntityObject("foo",
                EntityObject.EntityTypeEnum.BUY,
                0.5,
                EntityObject.CurrencyTypeEnum.SGP,
                LocalDate.of(2017,12,16),
                LocalDate.of(2017,12,17),
                100,
                10.25
        );

        EntityObject entityObjectSundayThursday = new EntityObject("bar",
                                                        EntityObject.EntityTypeEnum.BUY,
                                                        0.5,
                                                        EntityObject.CurrencyTypeEnum.AED,
                                                        LocalDate.of(2017,12,16),
                                                        LocalDate.of(2017,12,17),
                                                        100,
                                                        10.25
                                                );


        LocalDate settlementDate = entityObjectMondaySunday.getSettlementDate();
        entityObjectMondaySunday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectMondaySunday.getSettlementDate().getDayOfWeek());
        assertEquals(DayOfWeek.MONDAY, entityObjectMondaySunday.getSettlementDate().getDayOfWeek());

        settlementDate = entityObjectSundayThursday.getSettlementDate();
        entityObjectSundayThursday.moveSettlementToFirstPossibleDate();

        System.out.println(settlementDate.getDayOfWeek().toString()+"->"+ entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, entityObjectSundayThursday.getSettlementDate().getDayOfWeek());
    }
}
