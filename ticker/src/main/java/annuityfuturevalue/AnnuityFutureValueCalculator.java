package annuityfuturevalue;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnuityFutureValueCalculator {

    public static BigDecimal calculateFutureValue(BigDecimal annualPayment, BigDecimal annualRate, int years) {
        // 将年化收益率转换为小数形式
        BigDecimal rate = annualRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // 计算 (1 + rate)^years
        BigDecimal onePlusRate = BigDecimal.ONE.add(rate);
        BigDecimal onePlusRatePowerYears = onePlusRate.pow(years);

        // 计算 (1 + rate)^years - 1
        BigDecimal numerator = onePlusRatePowerYears.subtract(BigDecimal.ONE);

        // 计算 (1 + rate)^years - 1 / rate
        BigDecimal denominator = numerator.divide(rate, 10, RoundingMode.HALF_UP);

        // 计算未来值
        return annualPayment.multiply(denominator).setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        // 年度存款金额
        BigDecimal annualPayment = new BigDecimal("70000");
        // 年化收益率
        BigDecimal annualRate = new BigDecimal("15"); // 15%
        int years = 10;

        BigDecimal futureValue = calculateFutureValue(annualPayment, annualRate, years);
        System.out.println(years + "年后总存款金额: " + futureValue + " 元");
    }
}
