
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите арифметическое выражение: ");
        String input = scanner.nextLine();
        try {
            String result = calc(input);
            System.out.println("Результат: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String calc(String input) throws ArithmeticException {
        String[] tokens = input.split("\\s+");
        if (tokens.length != 3) {
            throw new ArithmeticException("Недопустимый формат ввода: " + input);
        }
        int operand1;
        int operand2;
        try {
            operand1 = parseOperand(tokens[0]);
            operand2 = parseOperand(tokens[2]);
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Недопустимый операнд: " + e.getMessage());
        }
        int result;
        switch (tokens[1]) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                result = operand1 / operand2;
                break;
            default:
                throw new ArithmeticException("Недопустимый оператор: " + tokens[1]);
        }
        if (result < 1) {
            throw new ArithmeticException("Результат должен быть больше 0: " + result);
        } else if (result > 10 && (isRomanNumeral(tokens[0]) || isRomanNumeral(tokens[2]))) {
            throw new ArithmeticException("Результат должен быть меньше или равен 10: " + result);
        } else if (isRomanNumeral(tokens[0]) && isRomanNumeral(tokens[2])) {
            result = parseRomanNumeral(toRomanNumeral(result));
        }
        return String.valueOf(result);
    }

    private static int parseOperand(String operand) throws NumberFormatException {
        if (isRomanNumeral(operand)) {
            return parseRomanNumeral(operand);
        } else {
            return Integer.parseInt(operand);
        }
    }

    private static boolean isRomanNumeral(String input) {
        return input.matches("[IVX]+");
    }

    private static int parseRomanNumeral(String input) throws NumberFormatException {
        int decimal = 0;
        int lastNumber = 0;
        String romanNumeral = input.toUpperCase();
        for (int i = romanNumeral.length() - 1; i >= 0; i--) {
            char ch = romanNumeral.charAt(i);
            switch (ch) {
                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;
                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;
                default:
                    throw new NumberFormatException("Недопустимая римская цифра: " + input);
            }
        }
        return decimal;
    }

    private static int processDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }

    private static String toRomanNumeral(int number) {
        int[] DECIMALS = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] ROMAN_NUMERALS = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
        StringBuilder roman = new StringBuilder();
        int i = 0;
        while (number > 0) {
            int k = number / DECIMALS[i];
            for (int j = 0; j < k; j++) {
                roman.append(ROMAN_NUMERALS[i]);
                number -= DECIMALS[i];
            }
            i++;
        }
        return roman.toString();
    }
}
