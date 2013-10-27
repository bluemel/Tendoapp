package de.bluemel.tendoapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.bluemel.tendoapp.shared.Seminar;
import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.Umlaut;

public class ModifySeminarDialog extends EnterSeminarDialog {

	public ModifySeminarDialog(final TendoApp app, final TendoAppServiceAsync service, final Seminar seminarToModify) {
		super(app, service, seminarToModify, Umlaut.Auml + "nderung eines geplanten Lehrgangs");
		super.okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				enterModifySeminar();
			}
		});
	}

	private void enterModifySeminar() {
		try {
			final SeminarDTO seminar = readSeminarFromUI(true);
			modifySeminar(seminar);
		} catch (OverlappingSeminarsException e) {
			final SeminarDTO seminar = readSeminarFromUI(false);
			final YesNoHandler yesNoHandler = new YesNoHandler() {

				@Override
				public void onYes() {
					modifySeminar(seminar);
				}

				@Override
				public void onNo() {
				}
			};
			new QuestionBox("Achtung Termin" + Umlaut.uuml + "berschneidung", e.getMessage()
					+ OverlappingSeminarsException.NEWLINE + "Termin trotzdem eintragen", yesNoHandler).show();
		} catch (IllegalArgumentException e) {
			new MessageBox("Eingabefehler", e.getMessage()).show();
		}
	}

	private void modifySeminar(final SeminarDTO seminar) {
		this.service.modifySeminar(seminar, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				new MessageBox("Unbekanter Fehler", "Fehler beim " + Umlaut.Auml + "ndern des Lehrgangs: "
						+ caught.getMessage()).show();
				closeDialog(false);
			}

			@Override
			public void onSuccess(Void result) {
				new MessageBox("Aktion erfolgreich", "Der Lehrgang wurde ge" + Umlaut.auml + "ndert.").show();
				closeDialog(true);
			}
		});
	}
}
