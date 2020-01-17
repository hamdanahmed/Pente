package Core;

import java.util.ArrayList;

/**
 *  represents the various game variants supported
 */
public enum GameVariant {
	PENTE_STANDARD(new GameRulePenteStandard(), "Pente - Standard"),
	PENTE_TOURNAMENT(new GameRulePenteTournament(), "Pente - Tournament"),
	PENTE_FREESTYLE(new GameRulePenteFreestyle(),"Pente - FreeStyle"),
	PENTE_NOCAPTURE(new GameRulePenteNoCaptures(), "Pente - No Captures"),
	PENTE_FIVEINAROW(new GameRulePenteFiveInARow(),"Pente -Five In A Row"),
	PENTE_KERYO(new GameRulePenteKeryo(),"Pente -Keryo"),
	GOMOKU_STANDARD(new GameRuleGomokuStandard(),"Gomoku -Standard"),
	GOMOKU_FREESTYLE(new GameRuleGomokuFreestyle(),"Gomoku - FreeStyle"),
	GOMOKU_CARO(new GameRuleGomokuCaro(),"Gomoku - Caro");
	

	private GameRule rule_;
	private String name_;

	private GameVariant ( GameRule rule, String name ) {
		rule_ = rule;
		name_ = name;
	}

	public static ArrayList<GameVariant> getGameVariants () {
		ArrayList<GameVariant> list = new ArrayList<GameVariant>();
		list.add(PENTE_STANDARD);
		list.add(PENTE_TOURNAMENT);
		list.add(PENTE_FREESTYLE);
		list.add(PENTE_NOCAPTURE);
		list.add(PENTE_FIVEINAROW);
		list.add(PENTE_KERYO);
		list.add(GOMOKU_STANDARD);
		list.add(GOMOKU_FREESTYLE);
		list.add(GOMOKU_CARO);
		return list;
	}

	public GameRule getRule () {
		return rule_;
	}
	
	public String getName () {
		return name_;
	}
}