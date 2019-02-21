package com.state.machine.config;

import com.state.machine.listener.MachineListener;
import com.state.machine.review.Events;
import com.state.machine.review.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.logging.Logger;

/**
 * Created by Dogukan Kundum on 21 Feb 2019.
 */
@Configuration
@EnableStateMachine
public class SubStateMachineConfiguration extends StateMachineConfigurerAdapter<States, Events> {

    public static final Logger LOGGER = Logger.getLogger(SubStateMachineConfiguration.class.getName());
/*
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.SI)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.SI).target(States.S1).event(Events.E1)
                .and()
                .withExternal()
                .source(States.S1).target(States.S2).event(Events.E2);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
*/
    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                    .initial(States.S1)
                    .state(States.S2)
                    .state(States.S3)

                .and()

                .withStates()
                    .parent(States.S2)
                    .initial(States.S21)
                    .entry(States.S2ENTRY)
                    .exit(States.S2EXIT)
                    .state(States.S22);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                    .source(States.S1).target(States.S2)
                    .event(Events.E1)

                .and()

                .withExternal()
                    .source(States.S2).target(States.S2ENTRY)
                    .event(Events.ENTRY)

                .and()

                .withExternal()
                    .source(States.S22).target(States.S2EXIT)
                    .event(Events.EXIT)

                .and()

                    .withEntry()
                    .source(States.S2ENTRY).target(States.S22)

                .and()

                    .withExit()
                    .source(States.S2EXIT).target(States.S3);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(new MachineListener() {
                });
    }

    @Bean
    public Guard<String, String> simpleGuard() {
        return (ctx) -> {
            int approvalCount = (int) ctx.getExtendedState().getVariables().getOrDefault("approvalCount", 0);
            return approvalCount > 0;
        };
    }

    @Bean
    public Action<String, String> entryAction() {
        return ctx -> LOGGER.info("Entry " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> doAction() {
        return ctx -> LOGGER.info("Do " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> executeAction() {
        return ctx -> {
            LOGGER.info("Execute " + ctx.getTarget().getId());
            int approvals = (int) ctx.getExtendedState().getVariables().getOrDefault("approvalCount", 0);
            approvals++;
            ctx.getExtendedState().getVariables().put("approvalCount", approvals);
        };
    }

    @Bean
    public Action<String, String> exitAction() {
        return ctx -> LOGGER.info("Exit " + ctx.getSource().getId() + " -> " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> errorAction() {
        return ctx -> LOGGER.info("Error " + ctx.getSource().getId() + ctx.getException());
    }

    @Bean
    public Action<String, String> initAction() {
        return ctx -> LOGGER.info(ctx.getTarget().getId());
    }
}