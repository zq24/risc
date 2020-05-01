package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {

    @Test
    void getActionType() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getActionType(), "type");
    }

    @Test
    void getSource() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getSource(), "1");
    }

    @Test
    void getDestination() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getDestination(), "2");
    }

    @Test
    void getNumOfUnits() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getNumOfUnits(), "3");
    }

    @Test
    void getUnitTypeFrom() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getStartUnitType(), "4");
        instruction.getAllianceId();
    }

    @Test
    void getUnitTypeTo() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.getEndUnitType(), "5");
    }

    @Test
    void createInstruction() {
        Instruction instruction = new Instruction();
    }

    @Test
    void instructionToString() {
        Instruction instruction = new Instruction("type", "1", "2", "3", "4", "5", "6");
        assertEquals(instruction.toString(), "Instruction{actionType='type', source='1', destination='2', numOfUnits='3'}");
    }
}