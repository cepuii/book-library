package ua.od.cepuii.library;

import ua.od.cepuii.library.util.PasswordUtil;

public class Demo {

    public static void main(String[] args) {
        String hash = "$argon2id$v=19$m=1048576,t=4,p=8$eUDJSTxGL9M4gXSbjX2Fmw$isGrGa+erC6lGq4ZyIGWkvVWloFLy/dWc0940QSZKzg";
        System.out.println(PasswordUtil.verify(hash, "qwe123".getBytes()));
    }

}
