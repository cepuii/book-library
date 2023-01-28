package ua.od.cepuii.library;

public class Demo {

    public static void main(String[] args) {
        System.out.println("Перевірка кирилиці".matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$"));
    }

}
