package com.kkaminsky.vktelegrambot.statemachine.listener

import com.kkaminsky.vktelegrambot.statemachine.event.BotEvent
import com.kkaminsky.vktelegrambot.statemachine.state.BotState
import org.springframework.messaging.Message
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.listener.StateMachineListener
import org.springframework.statemachine.state.State
import org.springframework.statemachine.transition.Transition
import java.lang.Exception

class BotStateMachineListener: StateMachineListener<BotState, BotEvent> {
    override fun stateMachineStopped(p0: StateMachine<BotState, BotEvent>?) {

    }

    override fun extendedStateChanged(p0: Any?, p1: Any?) {

    }

    override fun stateEntered(p0: State<BotState, BotEvent>?) {

    }

    override fun eventNotAccepted(p0: Message<BotEvent>?) {

    }

    override fun stateMachineStarted(p0: StateMachine<BotState, BotEvent>?) {

    }

    override fun stateContext(p0: StateContext<BotState, BotEvent>?) {

    }

    override fun stateChanged(from: State<BotState, BotEvent>?, to: State<BotState, BotEvent>?) {
        if(from==null && to!=null){
            System.out.println("Переход из null в "+ to.toString());
        }
        if(from!=null && to==null){
            System.out.println("Переход из "+ from.toString()+ " в null");
        }
        if(from==null && to==null){
            System.out.println("Переход из null в null");
        }
        if(from!=null && to!=null){
            System.out.println("Переход из "+ from.toString()+ " в "+ to.toString());
        }
        System.out.println("Переход из null в null");
        if (from != null && from.getId() != null && to!= null && to.getId() !=null ) {
            System.out.println("Переход из статуса " + from.getId() + " в статус " + to.getId());
        }

    }

    override fun transition(p0: Transition<BotState, BotEvent>?) {

    }

    override fun transitionEnded(p0: Transition<BotState, BotEvent>?) {

    }

    override fun transitionStarted(p0: Transition<BotState, BotEvent>?) {

    }

    override fun stateMachineError(p0: StateMachine<BotState, BotEvent>?, p1: Exception?) {

    }

    override fun stateExited(p0: State<BotState, BotEvent>?) {

    }
}