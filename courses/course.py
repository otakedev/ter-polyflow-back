import json

listeCours = {
    "EIINA902":["Architectures Logicielles 1","vendredi","matin",1],
    "EIINA900":["Architectures Logicielles 2","vendredi","matin",2],
    "EIINA904":["Rétro-Ingenierie, Maintenance et Evolution des Logiciels","mardi","matin",2],
    "EIINA901":["Architecture Logicielle pour le Cloud Computing","vendredi","apres-midi",1],
    "EIINA903":["Ingénierie des Modèles et Langages Spécifiques aux Domaines","mercredi","matin",2],
    "EIINA905":["SOA : Intégration de Services","lundi","apres-midi",1],
    "EIINC902":["Cybersécurité","vendredi","apres-midi",1],
    "EIINC904":["Sécurité dans les Réseaux","lundi","apres-midi",2],
    "EIINC905":["Sécurité des Applications Web","jeudi","matin",1],
    "EIINC901":["Cryptographie et Sécurité","lundi","apres-midi",1],
    "EIINC903":["Preuves en Cryptographie","mardi","matin",2],
    "EIIN945":["Security and Privacy 3.0","mercredi","apres-midi",2],
    "EIINI901":["Environnement Logiciels pour la Programmation Avancée de Terminaux Mobiles 1","mercredi","matin",1],
    "EIINI900":["Environnement Logiciels pour la Programmation Avancée de Terminaux Mobiles 2","mercredi","matin",2],
    "EIINI905":["Systèmes Intelligents Autonomes","mardi","matin",1],
    "EIINI902":["Middleware for the Internet of Things","mardi","matin",2],
    "EIINI903":["Objets Connectés et Services 1","mardi","apres-midi",1],
    "EIINI909":["Objets Connectés et Services 2","mardi","apres-midi",2],
    "EIINH901":["Adaptation des Interfaces à l'Environnement","vendredi","matin",1],
    "EIINH902":["Conception et Evaluation des IHM 1","lundi","matin",1],
    "EIINH900":["Conception et Evaluation des IHM 2","lundi","matin",2],
    "EIINH903":["Interfaces Réparties sur Plusieurs Supports","vendredi","matin",2],
    "EIINH904":["Interfaces Tactiles","vendredi","apres-midi",2],
    "EIINH905":["Techniques d Interaction et Multimodalité","jeudi","matin",1],
    "EIIN935":["Data Science","vendredi","apres-midi",1],
    "EIIN938":["Distributed Data Management","vendredi","matin",2],
    "EIIN947":["Big Data Technologies","lundi","matin",1],
    "EIIN934":["Applied Artificial Intelligence","lundi","apres-midi",1],
    "EIIN939":["Advanced Topics in Deep Learning","lundi","apres-midi",2],
    "EIIN948":["Machine Learning for Image Analysis","jeudi","matin",1],
    "EIIN941":["Ingénierie des Connaissances","mardi","apres-midi",1],
    "EIIN949":["Web de Données","mardi","matin",1],
    "EIIN951":["Web Sémantique","mardi","apres-midi",2],
    "EIIN937":["Fouilles de Données","mardi","matin",2],
    "EIIN927":["Programmable Web - Client-Side","jeudi","matin",2],
    "EIIN928":["Programmable Web - Server-Side","lundi","apres-midi",2],

    # Ubinet
    "EIIN907":["Graph Algorithms and Combinatorial Optimization","lundi","apres-midi",1],
    "EIIN936":["Machine Learning: Theory and Algorithms","mardi","apres-midi",1],
    "EIIN932":["Algorithmic Approach to Distributed Computing","lundi","matin",1],
    "EIIN943":["Large-Scale Distributed Systems","vendredi","apres-midi",1],
    "EIIN904":["Evolving Internet","vendredi","matin",1],
    "EIIN931":["Virtualized Infrastructure in Cloud Computing","lundi","matin",2],

    # cours d'option
    "EIIN901":["Administration Réseau","mardi","apres-midi",1],
    "EIIN902":["Algorithms for Telecommunication Networks","lundi","apres-midi",2],
    "EIIN933":["From Shallow to Deep Learning for Multimedia Data","mercredi","matin",2],
    "EIIN903B":["Multimedia Networking","mercredi","matin",1],
    "EIIN905B":["Model-Based Design for Cyber-Physical Systems and the Internet of Things","mardi","matin",1],
    "EIIN906B":["Blockchain and Privacy","jeudi","matin",2],
    "EIIN908B":["Data Mining for Networks","jeudi","apres-midi",2],
    "EIIN942":["Interagir dans un Monde 3D","mercredi","matin",1],
    "EIIN909":["Internet Measurements and New Architectures","vendredi","matin",2],
    "EIIN921":["Interprétation de Langages","lundi","apres-midi",2],
    "EIIN923":["Modélisation et Conception des Systèmes Embarqués","vendredi","apres-midi",2],
    "EIIN924":["Peer-to-Peer","mardi","matin",1],
    "EIIN925":["Performance Evaluation of Networks","mercredi","matin",2],
    "EIIN926B":["Programmation Fine et Complexité","mercredi","matin",1],
    "EIIN944":["Réalité Virtuelle","vendredi","apres-midi",2],
    "EIIN929":["Smart Cards","jeudi","matin",2],
    "EIIN946":["Techniques Modernes de Programmation Concurrentes","mardi","apres-midi",1],

    # divers (report, SHES, TER)
    "EIIN922":["Innovation & Recherche","","",3],
    "EIIN911":["Management & Ethique","jeudi","apres-midi",1],
    "EMESH301":["Techniques d'Expression","mercredi","apres-midi",1],
    "EIIN912":["Projet de Fin d Etudes","","",3],
    "EMOFI35":["Mineure DS4H","jeudi","matin","1"]
}   

listeCoursObligatoires = {
    'AL': ["EIINA902","EIINA900","EIINA904","EIINA901","EIINA903","EIINA905"],
    'CASPAR': ["EIINC902","EIINC904","EIINC905","EIINC901","EIINC903","EIIN945"],
    'IAM': ["EIINI901","EIINI900","EIINI905","EIINI902","EIINI903","EIINI909"],
    'IHM': ["EIINH901","EIINH902","EIINH900","EIINH903","EIINH904","EIINH905"],
    'SD': ["EIIN935","EIIN938","EIIN947","EIIN934","EIIN939","EIIN948"],
    'WEB': ["EIIN941","EIIN949","EIIN951","EIIN937","EIIN927","EIIN928"],
    'UBINET': ["EIIN907","EIIN936","EIIN932","EIIN943","EIIN904","EIIN931"]
}

listeCoursAssocies = {
    "EIIN927":["EIIN928"], # programmable web - client-side  => # programmable web - server-side
    "EIIN928":["EIIN927"], # programmable web - server-side  =>  # programmable web - client-side
    "EIINA902":["EIINA900"], # AL1  =>  # AL 2
    "EIINA900":["EIINA902"], # AL2  =>  # AL 1
    "EIINC903":["EIINC901"], # preuves en crypto  =>  # cryptographie
    "EIINI901":["EIINI900"], # ELIM 1  => # ELIM 2
    "EIINI900":["EIINI901"], # ELIM 2 => # ELIM 1
    "EIINI903":["EIINI909"], # OCS 1  =>  # OCS 2
    "EIINI909":["EIINI903"], # OCS 2  =>  # OCS 1
    "EIINH902":["EIINH900"], # CEIHM 1  =>  # CEIHM 2
    "EIINH900":["EIINH902"], # CEIHM 2  =>  # CEIHM 1
    "EIIN951":["EIIN949"], # Web Sémantique  =>  # Web de Données
    "EIIN902":["EIIN907"], #Algos for Telecom  =>  #Graph algos
    "EIINH904":["EIINH902","EIINH900","EIINH901", "EIINH905"],  # Interfaces tactiles  =>  # CEIHM 1 et 2 && # Adapt IHM && Techniques interaction et Multimodalite
    "EIINH903":["EIINH902","EIINH900","EIINH901", "EIINH905"]  # Interfaces multisupports  =>  # CEIHM 1 et 2 && #adapt IHM && Techniques interaction et Multimodalite
}

dates = ["lundi", "mardi", "mercredi", "jeudi", "vendredi"]

courses = []
for code, infos in listeCours.items():
    name = infos[0]
    date = dates.index(infos[1]) if infos[1] else None
    halfday = 0 if infos[2] == "matin" else 1 if infos[2] == 'apres-midi' else None
    period = infos[3]
    minor = [key for key, value in listeCoursObligatoires.items() if code in value]
    minor = minor[0] if minor else None
    constraints = listeCoursAssocies[code] if code in listeCoursAssocies else []
    res = {
        'period': period,
        'dayOfTheWeek': date,
        'code': code,
        'description': name,
        'halfDay': halfday,
        'minor': minor,
        'constraints': constraints
    }
    courses.append(res)

print(json.dumps(courses, ensure_ascii=False))