package de.bluemel.tendoapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.bluemel.tendoapp.shared.Seminar;
import de.bluemel.tendoapp.shared.Umlaut;

public class RemoveSeminarDialog extends EnterSeminarDialog {

	public RemoveSeminarDialog(final TendoApp app, final TendoAppServiceAsync service, final Seminar seminarToRemove) {
		super(app, service, seminarToRemove, "Entfernen eines geplanten Lehrgangs");
		// Add a handler to close the DialogBox
		super.okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				enterRemoveSeminar();
			}
		});
	}

	private void enterRemoveSeminar() {
		final Seminar seminar = readSeminarFromUI(false);
		if (seminar.getLocation().equals("sektion") && seminar.getAnnouncement().equals("tendo")) {
			removeSeminar(seminar);
		} else {
			new MessageBox("Keine Berechtigung", "Keine Berechtigung zum L" + Umlaut.ouml + "schen von Lehrg"
					+ Umlaut.auml + "ngen!!!").show();
			;
			closeDialog(false);
		}
	}

	private void removeSeminar(final Seminar seminar) {
		this.service.removeSeminar(seminar, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				new MessageBox("Unbekanter Fehler", "Fehler beim Entfernen des Lehrgangs: " + caught.getMessage())
						.show();
				closeDialog(false);
			}

			@Override
			public void onSuccess(Void result) {
				new MessageBox("Aktion erfolgreich", "Der Lehrgang wurde entfernt.").show();
				closeDialog(true);
			}
		});
	}
}
