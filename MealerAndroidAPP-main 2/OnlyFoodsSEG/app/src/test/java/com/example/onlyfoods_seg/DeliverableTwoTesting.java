package com.example.onlyfoods_seg;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DeliverableTwoTesting {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    Complaint sample  = new Complaint("8K7ZrBoAUpPk5erBxidT", "Bob", "three Michelin stars", "12");
    @Test
    public void testChefName(){
        assertEquals(sample.getChefName(), "Bob");
    }
    @Test
    public void testID(){
        assertEquals(sample.getId(), "8K7ZrBoAUpPk5erBxidT");
    }
    @Test
    public void testComplaintID(){
        assertEquals(sample.getComplaintId(), "12");
    }
    @Test
    public void testDescription(){
        assertEquals(sample.getDescription(), "three Michelin stars");
    }





}