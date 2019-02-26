package com.state.machine.config;

import com.state.machine.listener.MachineListener;
import com.state.machine.review.Events;
import com.state.machine.review.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Dogukan Kundum on 21 Feb 2019.
 */
@Configuration
@EnableStateMachine
public class SubStateMachineConfiguration extends StateMachineConfigurerAdapter<States, Events> {

    public static final Logger LOGGER = Logger.getLogger(SubStateMachineConfiguration.class.getName());

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states.withStates()
                .initial(States.INITIAL)
                .state(States.STARTED)
                .state(States.PREPROVISIONED)
                .and()
                .withStates()
                    .parent(States.INITIAL)
                    .initial(States.SAVED)
                    .state(States.SAVED)
                .and()
                .withStates()
                .state(States.ENABLED)
                .state(States.ACTIVATED)
                .state(States.ONOS_READY)
                .choice(States.ADD_CHOICE)
                .end(States.DONE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.INITIAL)
                .target(States.STARTED)
                .event(Events.REQUEST_RECEIVED)

                .and()

                .withExternal()
                .source(States.STARTED)
                .target(States.ADD_CHOICE)
                .event(Events.PREPROVISIONED)

                .and()

                .withChoice()
                .source(States.ADD_CHOICE)
                .first(States.PREPROVISIONED, PreprovisionedGuard())

                .and()

                .withExternal()
                .source(States.PREPROVISIONED)
                .target(States.SAVED)
                .event(Events.SAVED)

                .and()

                .withExternal()
                .source(States.SAVED)
                .target(States.ENABLED)
                .event(Events.ENABLED);

    }

    @Bean
    public Action<States, Events> test() {
        return new Action<States, Events>() {
            @Override
            public void execute(StateContext<States, Events> context) {
                context.getStateMachine().sendEvent(Events.ENABLED);
                return;
            }
        };
    }


    @Bean
    public Guard<States, Events> PreprovisionedGuard() {
        return new Guard<States, Events>() {

            @Override
            public boolean evaluate(StateContext<States, Events> context) {
                return context.getExtendedState().getVariables().containsKey("deviceAdded");
            }
        };
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