package haiti.server.gui;

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.LocaleManager;
import haiti.server.datamodel.AttributeManager.BeneficiaryCategory;
import haiti.server.datamodel.AttributeManager.BeneficiaryType;
import haiti.server.datamodel.Beneficiary;
import java.sql.*;
import java.util.Date;

//import com.sun.tools.javac.util.Log;

public class TbsManager {
	public static final String TAG = "TbsManager";
	public static final String RESULT_OK = "Ok";
	private Connection conn;
	private LocaleManager mLocaleManager;
	// public TbsManager() {}

	public TbsManager() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:acdi_source", "elidvert",
					"xyz%322");
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
		mLocaleManager = new LocaleManager();
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
			con = DriverManager.getConnection("jdbc:odbc:acdi_source", "elidvert",
					"xyz%322");

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

	// public Beneficiary getBeneficiaryByDossier(String dossier) {
	// String query = "select * from Beneficiaire where dossier = " + dossier;
	// ResultSet rs;
	// try {
	// Statement statement = conn.createStatement();
	// rs = statement.executeQuery(query);
	// while(!rs.isAfterLast()) {
	// String msg = DB_MESSAGE_ID + "=" + rs.getString(DB_MESSAGE_ID) +
	// AttributeManager.PAIRS_SEPARATOR
	// + DB_MESSAGE_SENDER+"=" +rs.getString(DB_MESSAGE_SENDER) +
	// AttributeManager.PAIRS_SEPARATOR
	// + DB_MESSAGE_STATUS + "=" + rs.getString(DB_MESSAGE_STATUS) +
	// AttributeManager.PAIRS_SEPARATOR
	// + DB_MESSAGE_TYPE + "=" +rs.getString(DB_MESSAGE_TYPE) +
	// AttributeManager.PAIRS_SEPARATOR
	// + DB_MESSAGE_CREATED_ON + ":" + rs.getString(DB_MESSAGE_CREATED_ON) +
	// AttributeManager.PAIRS_SEPARATOR
	// + DB_MESSAGE_MODIFIED_ON + ":" + rs.getString(DB_MESSAGE_MODIFIED_ON) +
	// AttributeManager.PAIRS_SEPARATOR
	// + rs.getString(DB_MESSAGE_COLUMN);
	// statusmsg.add(msg);
	// rs.next();
	// }
	// }
	// catch (SQLException e) {
	// System.out.println("SQL Exception");
	// e.printStackTrace();
	// }
	// return new Beneficiary();
	// }

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

		String info = beneficiary.getDossier() + "','"
				+ beneficiary.getLastName() + "','"
				+ beneficiary.getFirstName() + "','" + beneficiary.getCommune()
				+ "','" + beneficiary.getCommuneSection() + "','"
				+ beneficiary.getLocality() + "','" + (new Date()).getYear() + "/" + (new Date()).getMonth() + "/" + (new Date()).getDay()
				+ "','" + beneficiary.getDob() + "','" + beneficiary.getSex().name();

		String requete = "";

		BeneficiaryType bt = beneficiary.getBeneficiaryType();

		if (bt == BeneficiaryType.MCHN || bt == BeneficiaryType.BOTH) {
			requete = "Insert into Beneficiaire_1 values('" + info + "','"
					+ beneficiary.getBeneficiaryCategory().name() + "','"
					+ beneficiary.getNumberInHome() + "','"
					+ beneficiary.getHealthCenter() + "','"
					+ beneficiary.getDistributionPost() + "','"
					+ beneficiary.getHealthCenter() + "','"
					+ beneficiary.getGuardianChild() + "','"
					+ beneficiary.getGuardianWoman() + "','"
					+ beneficiary.getHusband() + "','"
					+ beneficiary.getFather() + "','"
					+ beneficiary.getIsMotherLeader().name() + "','"
					+ beneficiary.getVisitMotherLeader().name() + "','"
					+ beneficiary.getIsAgri().name() + "','"
					+ beneficiary.getAgriPerson() + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "','"
					+ " " + "')";
		}
		if (bt == BeneficiaryType.AGRI || bt == BeneficiaryType.BOTH) {
			requete = "Insert into MasterList_Livelihood values('" + info + "','"
					+ convertProfessionAttributes(beneficiary) + "','"
					+ beneficiary.getNumberInHome() + "','"
					+ beneficiary.getAmountOfLand() + "','"
					+ "BENEFICIARY SEEDS/FOODS" + "',"
					+ 0.0 + ",'"
					+ convertAgricultureAttributes(beneficiary) + "',"
					+ 0.0 + ",'"
					+ "BENEFICIARY UNITS" + "','"
					+ convertOrganizationAttributes(beneficiary) + "','"
					+ beneficiary.getIsHealth().name() + "','"
					+ beneficiary.getHealthPerson() + "','"
					+ "NAME OF AGRONOMIST" + "')";
		}
		System.out.println(requete);
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

	public String convertProfessionAttributes(Beneficiary beneficiary) {
		String professions = "";
		if (beneficiary.getIsArtisan()==AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_ARTISAN) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsFarmer()==AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_FARMER) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsMuso()==AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_MUSO) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsFisherman() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_FISHERMAN) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsRancher() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_CATTLE_RANCHER) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsStoreOwner() == AttributeManager.YnQuestion.Y)
			professions += LocaleManager.resources.getString(AttributeManager.FORM_STORE_OWNER) + " " + AttributeManager.ET_SEPARATOR + " ";
		professions = professions.substring(0,professions.length()-4); // Remove final 'et'
		return professions;
	}
	
	public String convertAgricultureAttributes(Beneficiary beneficiary) {
		String agricultureAttributes = "";
		if (beneficiary.getGetsCereal()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_CEREAL) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsHoe()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_HOE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsMachette()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_MACHETTE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsPelle()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_SHOVEL) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsPickaxe()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_PICKAXE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsSerpette()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_PRUNING_KNIFE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsTrees() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_TREE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsTubers() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_TREE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsVeggies() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_VEGETABLES) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsWheelbarrow() == AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_WHEELBARROW) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getGetsBarreAMines()==AttributeManager.YnQuestion.Y)
			agricultureAttributes += LocaleManager.resources.getString(AttributeManager.FORM_CROWBAR) + AttributeManager.ET_SEPARATOR + " ";
		agricultureAttributes = agricultureAttributes.substring(0,agricultureAttributes.length()-4); // Remove final 'et'
		return agricultureAttributes;
	}
	
	public String convertOrganizationAttributes(Beneficiary beneficiary){
		String organizations = "";
		if (beneficiary.getIsFAO()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_FAO) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsCROSE()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_CROSE) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsMARDNR()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_MARDNR) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsPLAN()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_PLAN) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsSAVE()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_SAVE_ORG) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (beneficiary.getIsOrganizationOther()==AttributeManager.YnQuestion.Y)
			organizations += LocaleManager.resources.getString(AttributeManager.FORM_OTHER_ORG) + " " + AttributeManager.ET_SEPARATOR + " ";
		if (!organizations.equals(""))
		organizations = organizations.substring(0,organizations.length()-4); // Remove final 'et'
		return organizations;
	}

	
	public static void main(String args[]){
		TbsManager tbs = new TbsManager();
		Beneficiary beneficiary = new Beneficiary("AV=186,mn=1:1,i=1,t=1,s=0,mi=0,ms=0,f=Michael,l=Phelps,ad=Baltimore,b=2011/7/18,g=M,n=2,la=29,cs=11eme La vallee,r2=Mama Phelps,pr=F,mo=0,is=1311,hs=795");
		System.out.println(tbs.convertProfessionAttributes(beneficiary) + " gg " + tbs.convertOrganizationAttributes(beneficiary) + " gg " + tbs.convertAgricultureAttributes(beneficiary));
		tbs.postNewBeneficiary(beneficiary);
	}
}
