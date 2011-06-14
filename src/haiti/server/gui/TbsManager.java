package haiti.server.gui;

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.Beneficiary;
import java.sql.*;

//import com.sun.tools.javac.util.Log;

public class TbsManager {
	public static final String TAG = "TbsManager";
	public static final String RESULT_OK = "Ok";
	private Connection conn;

	// public TbsManager() {}

	public TbsManager() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:acdi_source", "sa1",
					"tiovas");
		} catch (ClassNotFoundException f) {
			System.out.println("ClassNotFound Exception");
			f.printStackTrace();
		} catch (SQLException f) {
			System.out.println("SQL Exception");
			f.printStackTrace();
		} catch (Exception f) {
			System.out.println("Exception");
			f.printStackTrace();
		}
	}

	/**
	 * METHODE POUR FAIRE DES REQUETES
	 * 
	 * @param chaine
	 *            is the connection to the Db
	 * @param type
	 *            of update
	 */
	public String ExecuterRequete(String chaine, String type) {
		// chargement du Driver
		ResultSet result;
		int resultInt = 0;
		Connection con;
		try {
			// if (true)
			// throw new SQLException("bogus exception");
			//
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// etablir une connection
			con = DriverManager.getConnection("jdbc:odbc:acdi_source", "sa1",
					"tiovas");

			// Creation d'une instruction
			Statement requete = con.createStatement();

			// Instruction pour savoir le type de requete
			if ((type == "Inserer") || (type == "Mise") || (type == "Effacer")) {
				resultInt = requete.executeUpdate(chaine);
			}

			if (type == "Affiche") {
				result = requete.executeQuery(chaine);
			}
		} catch (ClassNotFoundException f) {
			System.out.println("ClassNotFound Exception");
			f.printStackTrace();
			return f.getMessage();
		} catch (SQLException f) {
			System.out.println("SQL Exception");
			f.printStackTrace();
			return f.getMessage();
			// JOptionPane.showMessageDialog(null,f,"Attention",JOptionPane.WARNING_MESSAGE);
		} catch (Exception f) {
			System.out.println("Exception");
			f.printStackTrace();
			return f.getMessage();
		}

		return RESULT_OK;
	}

	// Fin Methode pour faire des requettes

//	public Beneficiary getBeneficiaryByDossier(String dossier) {
//		String query = "select * from Beneficiaire where dossier = " + dossier;
//		ResultSet rs;
//		try {
//			Statement statement = conn.createStatement();
//			rs = statement.executeQuery(query);
//			while(!rs.isAfterLast()) {
//				String msg = DB_MESSAGE_ID + "=" +  rs.getString(DB_MESSAGE_ID) + AttributeManager.PAIRS_SEPARATOR 
//				+ DB_MESSAGE_SENDER+"=" +rs.getString(DB_MESSAGE_SENDER) + AttributeManager.PAIRS_SEPARATOR
//				+ DB_MESSAGE_STATUS + "=" + rs.getString(DB_MESSAGE_STATUS) + AttributeManager.PAIRS_SEPARATOR
//				+ DB_MESSAGE_TYPE + "=" +rs.getString(DB_MESSAGE_TYPE) + AttributeManager.PAIRS_SEPARATOR
//				+ DB_MESSAGE_CREATED_ON + ":" + rs.getString(DB_MESSAGE_CREATED_ON) + AttributeManager.PAIRS_SEPARATOR
//				+ DB_MESSAGE_MODIFIED_ON  + ":" + rs.getString(DB_MESSAGE_MODIFIED_ON) + AttributeManager.PAIRS_SEPARATOR
//				+ rs.getString(DB_MESSAGE_COLUMN);
//				statusmsg.add(msg);
//				rs.next();
//			}
//		}
//		catch (SQLException e) {
//			System.out.println("SQL Exception");
//			e.printStackTrace();
//		}
//		return new Beneficiary();
//	}

	/**
	 * Receives a Beneficiary object from the GUI and uses its data to update
	 * TBS Db.
	 * 
	 * @param beneficiary
	 * @return
	 */
	public String postNewBeneficiary(Beneficiary beneficiary) {
		// TODO: Write the necessary queries and return true
		// if insert was successful
		// //////////////////Partie Informations Generales

		String requete = "Insert into Beneficiaire_sms " + "values('"
				+ beneficiary.getRandomDossierNumber() + "','"
				+ beneficiary.getLastName() + "','"
				+ beneficiary.getFirstName() + "','" + beneficiary.getCommune()
				+ "','" + beneficiary.getCommuneSection() + "','"
				+ beneficiary.getAddress() + "')";
		String result = ExecuterRequete(requete, "Inserer");
		System.out.println(TAG + " result = " + result);
		return result;

		// "','"

		// +dateSaisie+"','"
		// +naissance+"','"
		// +sexe.getSelectedItem()+"','"
		// +type_field_benefi.getSelectedItem()+"','"
		// +type_field_benefi1.getSelectedItem()+"','"
		// +Nbre_personne_T.getText()+"',"
		// +ch_centre_sante.getSelectedItem()+"','"
		// +ch_centre_dist.getSelectedItem()+"','"
		// +ch_code_centre.getSelectedItem()+"','"
		// +responsable_T.getText()+"','"
		// +representant_FE_T.getText()+"','"
		// +Nom_epoux_T.getText()+"','"
		// +Nom_pere_T.getText()+"','"
		// +Mere_leader_T.getSelectedItem()+"','"
		// +recu_visit.getSelectedItem()+"','"
		// +Volet_agr_T.getSelectedItem()+"','"
		// +Reponse_T.getText()+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"','"
		// +" "+"')";
	}

}
