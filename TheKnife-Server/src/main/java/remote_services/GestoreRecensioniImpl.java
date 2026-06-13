package remote_services;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

import a_a.theknife.common.models.Recensione;
import a_a.theknife.common.models.Risposta;

import a_a.theknife.common.exceptions.AnswerAlreadyExistsException;
import a_a.theknife.common.exceptions.AnswerNotExistsException;
import a_a.theknife.common.exceptions.InvalidRatingException;
import a_a.theknife.common.exceptions.ReviewAlreadyExistsException;
import a_a.theknife.common.exceptions.ReviewNotExistsException;
import a_a.theknife.common.exceptions.TextTooLongException;
import a_a.theknife.common.interfaces.GestoreRecensioni;

public class GestoreRecensioniImpl extends UnicastRemoteObject implements GestoreRecensioni{
    public GestoreRecensioniImpl() throws RemoteException{
    }

    @Override
    public ArrayList<Recensione> visualizzaRecensioni(){
    }

    @Override
    public void scriviRecensione(Recensione recensione) throws RemoteException, InvalidRatingException, ReviewAlreadyExistsException{
    }

    @Override
    public void eliminaRecensione(Recensione recensione) throws RemoteException, ReviewNotExistsException{
    }

    @Override
    public void scriviRispostaRecensione(Risposta risposta) throws RemoteException, ReviewNotExistsException, AnswerAlreadyExistsException, TextTooLongException{
    }

    @Override
    public void modificaRisposta() throws RemoteException, AnswerNotExistsException{
    }
}