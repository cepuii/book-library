package ua.od.cepuii.library;

public class Demo {

    public static void main(String[] args) {
        System.out.println("Перевірка кирилиці".matches("^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\\-~`!@#$^&*()={}| ]{2,70}"));
    }

}
