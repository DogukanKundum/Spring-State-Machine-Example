package com.state.machine;

import com.state.machine.review.Events;
import com.state.machine.review.States;
import com.state.machine.config.SubStateMachineConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Dogukan Kundum on 21 Feb 2019.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SubStateMachineConfiguration.class)
public class StateMachineIntegrationTest {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Before
    public void setUp() {
        stateMachine.start();
    }

    @Test
    public void whenSimpleStringStateMachineEvents_thenEndState() {
//        assertEquals(States.SI, stateMachine.getState().getId());
    }
    @Test
    public void whenSimpleStringMachineActionState_thenActionExecuted() throws InterruptedException {

    }

    @After
    public void tearDown() {
        stateMachine.stop();
    }
}