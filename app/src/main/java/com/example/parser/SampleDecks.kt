package com.example.parser

import com.example.model.QcmDocument
import com.example.model.QcmOption
import com.example.model.QcmQuestion

object SampleDecks {

    fun getDefaultDecks(): List<QcmDocument> {
        val ultrasoundDeck = UltrasoundCourseDeck.getDocument()
        val cultureGenerale = QcmDocument(
            id = 101L,
            title = "QCM Culture Générale & Sciences",
            bestScore = 80,
            questions = listOf(
                QcmQuestion(
                    number = 1,
                    text = "Quel est le plus grand organe du corps humain ?",
                    options = listOf(
                        QcmOption("A", "Le foie", false),
                        QcmOption("B", "La peau", true),
                        QcmOption("C", "Le cerveau", false),
                        QcmOption("D", "Les poumons", false)
                    ),
                    correctLetter = "B",
                    explanation = "La peau est l'organe le plus étendu du corps humain avec une surface d'environ 1,5 à 2 m² et représente environ 16% de la masse corporelle chez l'adulte."
                ),
                QcmQuestion(
                    number = 2,
                    text = "Quelle est l'unité de mesure de la tension électrique dans le Système International ?",
                    options = listOf(
                        QcmOption("A", "L'Ampère", false),
                        QcmOption("B", "Le Watt", false),
                        QcmOption("C", "Le Volt", true),
                        QcmOption("D", "L'Ohm", false)
                    ),
                    correctLetter = "C",
                    explanation = "Le Volt (V) mesure la différence de potentiel ou tension électrique. L'Ampère mesure l'intensité et le Watt la puissance."
                ),
                QcmQuestion(
                    number = 3,
                    text = "En quelle année l'Homme a-t-il posé le pied sur la Lune pour la première fois ?",
                    options = listOf(
                        QcmOption("A", "1965", false),
                        QcmOption("B", "1969", true),
                        QcmOption("C", "1972", false),
                        QcmOption("D", "1961", false)
                    ),
                    correctLetter = "B",
                    explanation = "Neil Armstrong et Buzz Aldrin ont aluni le 20 juillet 1969 dans le cadre de la mission Apollo 11."
                ),
                QcmQuestion(
                    number = 4,
                    text = "Quel gaz est le principal composant de l'atmosphère terrestre ?",
                    options = listOf(
                        QcmOption("A", "Le dioxygène", false),
                        QcmOption("B", "Le diazote", true),
                        QcmOption("C", "Le dioxyde de carbone", false),
                        QcmOption("D", "L'argon", false)
                    ),
                    correctLetter = "B",
                    explanation = "L'atmosphère terrestre est composée d'environ 78% de diazote (N2) et 21% de dioxygène (O2)."
                ),
                QcmQuestion(
                    number = 5,
                    text = "Quelle molécule porte l'information génétique dans la plupart des organismes vivants ?",
                    options = listOf(
                        QcmOption("A", "L'ADN", true),
                        QcmOption("B", "L'ATP", false),
                        QcmOption("C", "L'ARN messager uniquement", false),
                        QcmOption("D", "L'hémoglobine", false)
                    ),
                    correctLetter = "A",
                    explanation = "L'ADN (acide désoxyribonucléique) contient l'ensemble des instructions génétiques nécessaires au développement et au fonctionnement des organismes."
                ),
                QcmQuestion(
                    number = 6,
                    text = "Qui a formulé la théorie de la relativité générale en 1915 ?",
                    options = listOf(
                        QcmOption("A", "Isaac Newton", false),
                        QcmOption("B", "Albert Einstein", true),
                        QcmOption("C", "Niels Bohr", false),
                        QcmOption("D", "Max Planck", false)
                    ),
                    correctLetter = "B",
                    explanation = "Albert Einstein a publié sa théorie de la relativité générale en novembre 1915, révolutionnant la gravitation."
                ),
                QcmQuestion(
                    number = 7,
                    text = "Quel est le fleuve le plus long d'Europe ?",
                    options = listOf(
                        QcmOption("A", "Le Danube", false),
                        QcmOption("B", "Le Rhin", false),
                        QcmOption("C", "La Volga", true),
                        QcmOption("D", "La Seine", false)
                    ),
                    correctLetter = "C",
                    explanation = "La Volga s'étend sur 3 530 kilomètres en Russie occidentale avant de se jeter dans la mer Caspienne."
                ),
                QcmQuestion(
                    number = 8,
                    text = "Quelle planète du système solaire possède le plus de satellites naturels découverts à ce jour ?",
                    options = listOf(
                        QcmOption("A", "Jupiter", false),
                        QcmOption("B", "Saturne", true),
                        QcmOption("C", "Uranus", false),
                        QcmOption("D", "Mars", false)
                    ),
                    correctLetter = "B",
                    explanation = "Saturne compte plus de 140 lunes confirmées, dépassant Jupiter dans les recensements astronomiques récents."
                ),
                QcmQuestion(
                    number = 9,
                    text = "Combien de chromosomes comporte une cellule humaine somatique normale ?",
                    options = listOf(
                        QcmOption("A", "23", false),
                        QcmOption("B", "46", true),
                        QcmOption("C", "48", false),
                        QcmOption("D", "92", false)
                    ),
                    correctLetter = "B",
                    explanation = "Les cellules somatiques humaines sont diploïdes et contiennent 46 chromosomes (23 paires)."
                ),
                QcmQuestion(
                    number = 10,
                    text = "Quel élément chimique a pour symbole 'Au' dans le tableau périodique ?",
                    options = listOf(
                        QcmOption("A", "L'argent", false),
                        QcmOption("B", "L'or", true),
                        QcmOption("C", "L'aluminium", false),
                        QcmOption("D", "L'uranium", false)
                    ),
                    correctLetter = "B",
                    explanation = "Le symbole de l'or est 'Au', issu du latin 'Aurum' (aurore étincelante)."
                )
            )
        )

        val informatique = QcmDocument(
            id = 102L,
            title = "QCM Informatique & Réseaux",
            bestScore = null,
            questions = listOf(
                QcmQuestion(
                    number = 1,
                    text = "Quel protocole réseau fonctionne sur le port par défaut 443 ?",
                    options = listOf(
                        QcmOption("A", "HTTP", false),
                        QcmOption("B", "SSH", false),
                        QcmOption("C", "HTTPS", true),
                        QcmOption("D", "DNS", false)
                    ),
                    correctLetter = "C",
                    explanation = "HTTPS utilise le port standard 443 pour le trafic web chiffré via TLS/SSL."
                ),
                QcmQuestion(
                    number = 2,
                    text = "Quelle est la complexité temporelle moyenne d'une recherche dans une table de hachage (HashMap) bien dimensionnée ?",
                    options = listOf(
                        QcmOption("A", "O(1)", true),
                        QcmOption("B", "O(log n)", false),
                        QcmOption("C", "O(n)", false),
                        QcmOption("D", "O(n log n)", false)
                    ),
                    correctLetter = "A",
                    explanation = "En moyenne, l'accès par clé dans une table de hachage se fait en temps constant O(1)."
                ),
                QcmQuestion(
                    number = 3,
                    text = "Dans le modèle OSI, à quelle couche appartient le protocole IP (Internet Protocol) ?",
                    options = listOf(
                        QcmOption("A", "Couche 2 - Liaison", false),
                        QcmOption("B", "Couche 3 - Réseau", true),
                        QcmOption("C", "Couche 4 - Transport", false),
                        QcmOption("D", "Couche 7 - Application", false)
                    ),
                    correctLetter = "B",
                    explanation = "IP est le protocole fondamental de la couche réseau (Layer 3), assurant le routage des paquets."
                ),
                QcmQuestion(
                    number = 4,
                    text = "Quel paradigme de programmation est au cœur de Jetpack Compose ?",
                    options = listOf(
                        QcmOption("A", "UI impérative basée sur des vues XML", false),
                        QcmOption("B", "UI déclarative et réactive", true),
                        QcmOption("C", "Programmation orientée aspects pure", false),
                        QcmOption("D", "Programmation logique pure", false)
                    ),
                    correctLetter = "B",
                    explanation = "Jetpack Compose est une boîte à outils déclarative moderne pour créer des interfaces utilisateur natives sur Android."
                ),
                QcmQuestion(
                    number = 5,
                    text = "Que signifie l'acronyme SQL ?",
                    options = listOf(
                        QcmOption("A", "Structured Query Language", true),
                        QcmOption("B", "Standard Question Logic", false),
                        QcmOption("C", "System Query Link", false),
                        QcmOption("D", "Simple Query Layout", false)
                    ),
                    correctLetter = "A",
                    explanation = "SQL signifie Structured Query Language, langage standardisé pour manipuler les bases de données relationnelles."
                )
            )
        )

        return listOf(ultrasoundDeck, cultureGenerale, informatique)
    }

    fun get10TestQcmDocument(): QcmDocument {
        return QcmDocument(
            id = 2001L,
            title = "10 QCM de Test (Culture & Cameroun)",
            bestScore = null,
            questions = listOf(
                QcmQuestion(
                    number = 1,
                    text = "Quelle est la capitale politique du Cameroun ?",
                    options = listOf(
                        QcmOption("A", "Douala", false),
                        QcmOption("B", "Yaoundé", true),
                        QcmOption("C", "Garoua", false),
                        QcmOption("D", "Bafoussam", false)
                    ),
                    correctLetter = "B",
                    explanation = "Yaoundé, surnommée la 'ville aux sept collines', est la capitale politique et administrative du Cameroun."
                ),
                QcmQuestion(
                    number = 2,
                    text = "Quel est le point culminant du Cameroun et d'Afrique centrale ?",
                    options = listOf(
                        QcmOption("A", "Le Mont Kilimandjaro", false),
                        QcmOption("B", "Le Mont Cameroun (Mont Fako)", true),
                        QcmOption("C", "Le Mont Manengouba", false),
                        QcmOption("D", "Le Mont Oku", false)
                    ),
                    correctLetter = "B",
                    explanation = "Le Mont Cameroun (ou Fako) culmine à 4 095 mètres d'altitude, ce qui en fait le plus haut sommet d'Afrique centrale."
                ),
                QcmQuestion(
                    number = 3,
                    text = "Quelle est la devise nationale du Cameroun ?",
                    options = listOf(
                        QcmOption("A", "Liberté, Égalité, Fraternité", false),
                        QcmOption("B", "Paix - Travail - Patrie", true),
                        QcmOption("C", "Unité - Progrès - Justice", false),
                        QcmOption("D", "Union - Discipline - Travail", false)
                    ),
                    correctLetter = "B",
                    explanation = "La devise officielle de la République du Cameroun est 'Paix - Travail - Patrie' ('Peace - Work - Fatherland')."
                ),
                QcmQuestion(
                    number = 4,
                    text = "Quel est le plus grand organe du corps humain ?",
                    options = listOf(
                        QcmOption("A", "Le foie", false),
                        QcmOption("B", "La peau", true),
                        QcmOption("C", "Le cerveau", false),
                        QcmOption("D", "Les poumons", false)
                    ),
                    correctLetter = "B",
                    explanation = "La peau est l'organe le plus étendu du corps humain avec une surface d'environ 1,5 à 2 m² et représente environ 16% du poids corporel."
                ),
                QcmQuestion(
                    number = 5,
                    text = "Quelle est l'unité de mesure de la tension électrique dans le Système International ?",
                    options = listOf(
                        QcmOption("A", "L'Ampère", false),
                        QcmOption("B", "Le Watt", false),
                        QcmOption("C", "Le Volt", true),
                        QcmOption("D", "L'Ohm", false)
                    ),
                    correctLetter = "C",
                    explanation = "Le Volt (V) mesure la différence de potentiel ou tension électrique. L'Ampère mesure l'intensité."
                ),
                QcmQuestion(
                    number = 6,
                    text = "En quelle année le Cameroun a-t-il accédé à son indépendance ?",
                    options = listOf(
                        QcmOption("A", "1958", false),
                        QcmOption("B", "1960", true),
                        QcmOption("C", "1962", false),
                        QcmOption("D", "1972", false)
                    ),
                    correctLetter = "B",
                    explanation = "Le Cameroun a proclamé son indépendance le 1er janvier 1960."
                ),
                QcmQuestion(
                    number = 7,
                    text = "Quelle molécule porte l'information génétique dans la cellule vivante ?",
                    options = listOf(
                        QcmOption("A", "L'ADN", true),
                        QcmOption("B", "L'ATP", false),
                        QcmOption("C", "Le glycogène", false),
                        QcmOption("D", "L'hémoglobine", false)
                    ),
                    correctLetter = "A",
                    explanation = "L'ADN (acide désoxyribonucléique) contient l'ensemble des instructions génétiques nécessaires au fonctionnement des organismes."
                ),
                QcmQuestion(
                    number = 8,
                    text = "Quel célèbre parc national de la région de l'Extrême-Nord du Cameroun est réputé pour sa faune sahélienne ?",
                    options = listOf(
                        QcmOption("A", "Le Parc national de Waza", true),
                        QcmOption("B", "Le Parc national de Korup", false),
                        QcmOption("C", "Le Parc national de Campo-Ma'an", false),
                        QcmOption("D", "Le Parc national de Lobéké", false)
                    ),
                    correctLetter = "A",
                    explanation = "Le Parc national de Waza est l'un des joyaux naturels du Cameroun, abritant éléphants, girafes, lions et de nombreuses espèces d'oiseaux."
                ),
                QcmQuestion(
                    number = 9,
                    text = "Combien de chromosomes comporte une cellule somatique humaine normale ?",
                    options = listOf(
                        QcmOption("A", "23", false),
                        QcmOption("B", "46", true),
                        QcmOption("C", "48", false),
                        QcmOption("D", "92", false)
                    ),
                    correctLetter = "B",
                    explanation = "Les cellules somatiques humaines sont diploïdes et contiennent 46 chromosomes (23 paires)."
                ),
                QcmQuestion(
                    number = 10,
                    text = "Quel élément chimique a pour symbole 'Au' dans le tableau périodique ?",
                    options = listOf(
                        QcmOption("A", "L'argent", false),
                        QcmOption("B", "L'or", true),
                        QcmOption("C", "L'aluminium", false),
                        QcmOption("D", "L'uranium", false)
                    ),
                    correctLetter = "B",
                    explanation = "Le symbole de l'or est 'Au', issu du mot latin 'Aurum' (aurore étincelante)."
                )
            )
        )
    }

    val samplePasteText = """
1. Quelle est la capitale de l'Australie ?
A. Sydney
B. Melbourne
C. Canberra
D. Brisbane

2. Quel physicien a découvert la radioactivité en 1896 ?
A. Pierre Curie
B. Henri Becquerel
C. Ernest Rutherford
D. Wilhelm Röntgen

3. Quelle est la vitesse de la lumière dans le vide ?
A. Environ 300 000 km/s
B. Environ 150 000 km/s
C. Environ 3 000 000 km/s
D. Environ 30 000 km/s

Corrigé commenté
1 C
2 B
3 A

Explications
1 C Canberra a été choisie comme capitale en 1908 pour mettre fin à la rivalité entre Sydney et Melbourne.
2 B Henri Becquerel a découvert la radioactivité spontanée en étudiant la phosphorescence des sels d'uranium.
3 A La constante c vaut exactement 299 792 458 m/s, soit environ 300 000 km/s.
""".trimIndent()
}
