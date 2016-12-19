package id.smarta.krakatau.streamer.twitter;

public enum TwitterUserID {
	detikcom(69183155l),
	kompascom(23343960l),	
	tempodotco(18129942l),
	liputan6dotcom(47596019l),
	SindoRCTI(132078023l),
	OfficialiNewsTV(375995332l),
	lensaRTV(2393930923l),
	OfficialNETNews(1963857720l),
	Lintas_MNCTV(61377303l),
	CNNIndonesia(17128975l),
	Metro_TV(57261519l),
	tvOneNews(55507370l),
	mediaindonesia(68930552l),
	jawapos(2392460208l),
	republikaonline(22126902l),
	korantempo(20963426l),
	VIVAcoid(41730943l),
	antaranews(18071520l),
	Beritasatu(154102750l),
	hariankompas(255866913l),
	tribunnews(124171593l),
	bbcindonesia(23772644l),
	inilahdotcom(46693036l),
	okezonenews(47274731l),
	dailysocial(125086845l),
	techinasia(1578524700l);

	private Long userId;

	private TwitterUserID(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}
}
