import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> vocabulary = buildVocabulary();

    public static void main(String[] args) {

        while (true) {
            System.out.println("Хотите начать новую игру? [Д], [Н]");
            String inputString = scanner.nextLine();
            if (isInputStringValid(inputString)) {
                if (inputString.equals("Д")) {
                    play();
                } else {
                    break;
                }
            } else {
                System.out.println("Некоректный ответ, введите правильный символ : [Д] или [Н]");
            }

        }

    }

    private static boolean isLowerCaseRussianChar(String string) {
        if (string.length() > 1) {
            return false;
        }
        char userInput = string.charAt(0);
        return (Character.UnicodeBlock.of(userInput) == Character.UnicodeBlock.CYRILLIC) && (Character.isLowerCase(userInput));
    }

    private static boolean isInputStringValid(String str) {
        return str.equals("Д") || str.equals("Н");
    }

    private static List<String> buildVocabulary() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Words.txt"))) {
            String word;
            while ((word = reader.readLine()) != null) {
                list.add(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static void play() {

        Random random = new Random();
        String mysteryWord = vocabulary.get(random.nextInt(vocabulary.size()));
        System.out.println(mysteryWord);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-".repeat(mysteryWord.length()));
        System.out.println(stringBuilder);

        int losses = 0;
        Set<Character> allInputChars = new HashSet<>();
        while (losses != 6 && (!stringBuilder.toString().equals(mysteryWord))) {
            String inputLine = scanner.nextLine();
            if (isLowerCaseRussianChar(inputLine)) {
                char inputChar = inputLine.charAt(0);
                if (allInputChars.add(inputChar)) {
                    int indexOfCharInString = mysteryWord.indexOf(inputChar);
                    if (indexOfCharInString > -1) {
                        while (indexOfCharInString > -1) {
                            stringBuilder.setCharAt(indexOfCharInString, inputChar);
                            indexOfCharInString = mysteryWord.indexOf(inputChar, indexOfCharInString + 1);
                        }
                    } else {
                        losses++;
                        System.out.println("Количество ошибок: " + losses);
                    }

                } else {
                    System.out.println("Вы уже вводили этот символ раньше, будьте внимательнее!");
                }
            } else {
                System.out.println("Неверный ввод! Введите один символ из кириллицы в нижнем регистре");
            }
            System.out.println(stringBuilder);
        }
        if (losses == 6){
            System.out.println("Вы проиграли(((");
        } else {
            System.out.println("Поздравляю, вы победили!!!");
        }

    }
}
