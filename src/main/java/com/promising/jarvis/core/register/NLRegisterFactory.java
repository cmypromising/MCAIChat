package com.promising.jarvis.core.register;

import com.promising.jarvis.core.register.impl.CommandRegister;

import java.util.ArrayList;
import java.util.List;

public class NLRegisterFactory {

    public static List<NLRegister> createRegisters(){
        List<NLRegister> registers = new ArrayList<>();
        registers.add(new CommandRegister());
        return registers;
    }
}
