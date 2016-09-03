package lv.mmm.services.defaultValueAppliers;

import lv.mmm.domain.Loan;

import java.util.Calendar;

public class LoanDefaultValueApplier {

    public static void apply(Loan loan) {
        loan.setApplicationDate(Calendar.getInstance().getTime());
    }
}
