package com.example.parser

import com.example.model.QcmDocument

object UltrasoundCourseDeck {

    val courseRawText = """
100 QCM de révision : Généralités sur l'ultrasonographie
Chapitre I · La sonde, l'onde ultrasonore et ses interactions avec les tissus · Une seule bonne réponse par question. Corrigé commenté en fin de document.

Partie 1 · Introduction et appareil
1. Quelle affirmation sur l'échographie est exacte ?
A. Elle est indépendante de l'opérateur
B. Elle utilise des rayons X de faible dose
C. Elle n'utilise pas de rayonnement ionisant
D. Elle exige toujours une injection de produit de contraste

2. Que permet de voir l'échographie dans un même examen ?
A. Uniquement la morphologie
B. Uniquement les flux
C. Uniquement le métabolisme cellulaire
D. La morphologie (mode B) et les flux (Doppler)

3. Les normes d'intensité sont plus restrictives pour :
A. Le genou et la hanche
B. La femme enceinte et l'examen de l'œil
C. Le sujet âgé
D. L'abdomen de l'adulte

4. À quoi servent les curseurs TGC ?
A. À changer de mode
B. À mesurer des distances
C. À régler le gain selon la profondeur
D. À choisir la sonde

5. À quoi sert le trackball ?
A. Régler la fréquence de la sonde
B. Pointage, curseurs et mesures
C. Restituer le son Doppler
D. Congeler l'image uniquement

6. Que restituent les haut-parleurs de l'appareil ?
A. La fréquence de la sonde
B. Les alarmes de sécurité uniquement
C. Le son du Doppler
D. Le bruit des organes

7. Concernant le gain :
A. Le gain est unique pour tous les modes
B. Le gain ne dépend jamais de la profondeur
C. Le gain n'existe qu'en Doppler
D. Chaque mode a son propre gain

8. Pourquoi dit-on que l'échographie est opérateur-dépendante ?
A. L'appareil règle tout seul
B. L'image est fabriquée et interprétée pendant l'acquisition
C. Les images sont lues uniquement le lendemain
D. Elle est toujours automatisée

Partie 2 · La sonde
9. La piézo-électricité est la propriété de certains cristaux de :
A. Convertir lumière en chaleur
B. Produire des rayons X
C. Convertir énergie mécanique ↔ énergie électrique
D. Stocker de la chaleur

10. À l'émission, que se passe-t-il dans le cristal ?
A. Un écho le déforme et crée une tension
B. Il absorbe les ultrasons
C. Il chauffe sans vibrer
D. Une tension électrique le déforme : il vibre et émet des ultrasons

11. À la réception, que se passe-t-il dans le cristal ?
A. Il amplifie le gel
B. L'écho le déforme et produit une tension (signal)
C. Il émet une onde
D. Il se refroidit

12. Dans une sonde échographique, le cristal est :
A. Ni l'un ni l'autre
B. Uniquement émetteur
C. Uniquement récepteur
D. À la fois émetteur et récepteur

13. Au repos, dans un cristal piézo-électrique :
A. Le cristal est polarisé en permanence
B. Il existe un fort champ électrique
C. Les centres de gravité des charges + et − sont confondus
D. Les charges + et − sont séparées

14. Sous contrainte mécanique, un cristal piézo-électrique :
A. Reste neutre
B. Devient supraconducteur
C. Se polarise : une différence de potentiel apparaît entre ses faces
D. Perd toute charge

15. Quel est le rôle de l'amortisseur ?
A. Adapter l'impédance vers les tissus
B. Augmenter la fréquence
C. Appliquer la tension au cristal
D. Bloquer les ondes vers l'arrière et stopper les oscillations parasites

16. Où se situe l'amortisseur dans la sonde ?
A. À l'arrière du cristal
B. À l'avant, au contact de la peau
C. Entre le gel et la peau
D. Il n'y en a pas

17. Quel est le rôle de la lame d'adaptation (λ/4) ?
A. Adapter l'impédance entre le cristal et les tissus
B. Refroidir le cristal
C. Amortir les vibrations
D. Amplifier le signal reçu

18. Quel est l'ordre exact des éléments, de l'arrière vers l'avant ?
A. Électrode, lame, amortisseur, cristal
B. Amortisseur, électrode, cristal, électrode, lame d'adaptation, gel
C. Cristal, amortisseur, lame, électrode, gel
D. Gel, lame, cristal, amortisseur

19. À quoi servent les électrodes ?
A. Mesurer la température
B. Appliquer et recueillir la tension sur chaque face du cristal
C. Protéger la peau
D. Absorber les ultrasons

20. L'impédance acoustique Z des tissus mous vaut environ :
A. 150 MRayl
B. 15 MRayl
C. 0,15 MRayl
D. 1,5 MRayl

21. Que mesure Kt ?
A. L'efficacité de la conversion déformation ↔ tension
B. La densité du cristal
C. La célérité du son
D. L'atténuation du gel

22. Le matériau idéal pour une sonde possède :
A. Un Kt faible et un Z élevé
B. Un Kt élevé et un Z proche de celui des tissus
C. Un Kt faible et un Z proche de l'air
D. Un Kt élevé et un Z très élevé

23. Quel est le principal défaut du quartz ?
A. Il absorbe tous les ultrasons
B. Il est trop léger pour vibrer
C. Il n'est pas piézo-électrique
D. Z très élevée, Kt médiocre, difficile à découper en petits éléments

24. Les céramiques synthétiques (PZT) se caractérisent par :
A. Une impédance identique à celle des tissus
B. Un meilleur Kt mais une impédance encore élevée
C. Une absence de piézo-électricité
D. Un Kt très faible

25. Les polymères (PVDF) ont :
A. Une impédance très élevée
B. Une impédance plus proche des tissus mais un Kt plus faible
C. Un Kt maximal
D. Aucune utilité en imagerie

26. Les composites céramique + polymère sont :
A. Réservés à la radiothérapie
B. Un matériau abandonné
C. Un matériau non piézo-électrique
D. Le compromis actuel, surtout pour le Kt

27. Les CMUT fonctionnent grâce à :
A. Des forces électrostatiques (pas la piézo-électricité)
B. La piézo-électricité du quartz
C. La résonance magnétique
D. La radioactivité

28. Un élément CMUT est constitué de :
A. Une petite cellule vide (quelques µm) sur un support de silicium
B. Un gros cristal de quartz
C. Un fil de cuivre nu
D. Une bille de plomb

29. Quel est l'intérêt principal des CMUT ?
A. L'absence d'électronique
B. Une impédance identique à celle de l'air
C. Un Kt nul
D. Matrices 3D/4D et miniaturisation (fabrication type circuit intégré)

Partie 3 · L'onde ultrasonore
30. Un ultrason est :
A. Une onde électromagnétique
B. Une onde mécanique de pression
C. Un flux de particules chargées
D. Une onde lumineuse

31. Les ultrasons se propagent :
A. Seulement dans l'air
B. Dans le vide
C. Surtout dans le vide
D. Uniquement dans un milieu matériel

32. Que transporte une onde ultrasonore ?
A. De l'énergie, sans transport de matière
B. De la matière et de l'énergie
C. De la matière uniquement
D. Des charges électriques

33. La célérité moyenne des ultrasons dans les tissus mous est :
A. ≈ 15 m/s
B. ≈ 330 m/s
C. ≈ 1540 m/s
D. ≈ 340 000 m/s

34. La longueur d'onde se calcule par :
A. λ = c × f
B. λ = 1 / c
C. λ = c / f
D. λ = f / c

35. La période T est :
A. T = f
B. T = c / f
C. T = 1/f
D. T = f²

36. À 1 MHz dans les tissus mous, λ vaut environ :
A. 0,15 mm
B. 0,31 mm
C. 1,54 mm
D. 15,4 mm

37. À 10 MHz dans les tissus mous, λ vaut environ :
A. 0,15 mm
B. 0,31 mm
C. 1,54 mm
D. 1,5 cm

38. Quand la fréquence augmente :
A. La résolution diminue
B. La longueur d'onde augmente
C. La célérité augmente fortement
D. La longueur d'onde diminue et les détails sont plus fins

39. Le domaine des ultrasons est :
A. 20 kHz – 200 MHz
B. 20 Hz – 20 kHz
C. < 20 Hz
D. > 200 MHz

40. Quelle est la gamme habituelle de l'échographie diagnostique ?
A. 1 à 15 MHz
B. 100 à 500 MHz
C. 1 à 15 kHz
D. 20 à 200 kHz

41. Quelles applications utilisent jusqu'à 50 MHz ?
A. Endovasculaire, peau, segment antérieur de l'œil
B. Abdomen de l'adulte
C. Cardiologie
D. Doppler transcrânien

42. Une sonde de 1–3 MHz est surtout utilisée en :
A. Étude de la thyroïde
B. Imagerie de l'œil
C. Cardiologie et Doppler transcrânien
D. Dermatologie

43. L'abdomen de l'adulte et l'obstétrique utilisent en général :
A. > 100 MHz
B. ≈ 0,5 MHz
C. Environ 3–5 MHz
D. 20–50 MHz

44. Les vaisseaux périphériques et organes superficiels (thyroïde, sein) utilisent :
A. 3–5 MHz
B. > 100 MHz
C. Environ 7–15 MHz
D. 1–3 MHz

45. Quelle fréquence est adaptée à la pédiatrie, aux muscles et tendons ?
A. Environ 5–7 MHz
B. 20–50 MHz
C. < 1 MHz
D. 1 MHz

46. Quelle est la règle de choix de la fréquence ?
A. Toujours 3,5 MHz
B. La plus basse possible
C. La plus haute possible, quelle que soit la cible
D. La plus haute qui permet encore d'atteindre la cible

47. La formule du décibel est :
A. β = 10 · log (I / I₀)
B. β = I / I₀
C. β = log (I₀ / 10)
D. β = 10 · I₀

48. Une augmentation de + 10 dB correspond à une intensité :
A. Divisée par 10
B. Multipliée par 10
C. Multipliée par 100
D. Multipliée par 2

49. Une augmentation de + 3 dB correspond environ à :
A. Une intensité multipliée par 2
B. Une intensité multipliée par 10
C. Une intensité multipliée par 3
D. Une intensité divisée par 2

50. Une augmentation de + 20 dB correspond à :
A. Une intensité multipliée par 100
B. Une intensité multipliée par 10
C. Une intensité multipliée par 20
D. Une intensité multipliée par 1000

51. Le seuil d'audition I₀ correspond à :
A. 10¹² W/m²
B. 1 W/m²
C. 10⁻³ W/m²
D. 10⁻¹² W/m² (0 dB)

52. La dynamique d'un appareil d'échographie actuel est :
A. ≈ 12 dB
B. ≈ 1,2 dB
C. ≈ 30 dB
D. ≥ 120 dB

53. L'impédance acoustique est donnée par :
A. Z = c / f
B. Z = ρ × c
C. Z = ρ × f
D. Z = ρ / c

54. Quelle est la célérité approximative dans l'air ?
A. ≈ 4000 m/s
B. ≈ 15 000 m/s
C. ≈ 1540 m/s
D. ≈ 330 m/s

55. Les appareils d'échographie sont calibrés sur :
A. c = 330 m/s
B. c = 3 × 10⁸ m/s
C. c = 4000 m/s
D. c = 1540 m/s

56. Quel milieu a l'impédance la plus élevée ?
A. La graisse
B. Le sang
C. L'air
D. L'os

57. La célérité dans la graisse est :
A. Égale à celle de l'os
B. Très supérieure à celle de l'eau
C. Nulle
D. Un peu inférieure à celle de l'eau (≈ 1450 m/s)

Partie 4 · Interactions avec les tissus
58. La réflexion se produit :
A. Sur les interfaces entre tissus d'impédances différentes
B. Sur des cibles plus petites que λ
C. Uniquement dans le vide
D. Dans un milieu parfaitement homogène

59. Que représente la réflexion à l'écran ?
A. Les contours des organes
B. La texture des parenchymes
C. L'absorption
D. Le signal Doppler

60. La diffusion est à l'origine :
A. Des contours des organes
B. De l'ombre acoustique
C. De la texture des parenchymes et du signal Doppler
D. Du gel

61. Que devient l'énergie absorbée par les tissus ?
A. Elle se transforme en chaleur (perte de signal)
B. Elle renforce le signal profond
C. Elle crée l'image des contours
D. Elle supprime le speckle

62. L'atténuation est la somme de :
A. Réfraction + diffraction
B. Absorption + réflexion + diffusion
C. Réflexion seule
D. Absorption + gel

63. L'intensité d'une onde diminue avec la profondeur selon :
A. Une loi constante
B. Une loi sinusoïdale
C. Une loi exponentielle I = I₀ · e^(−µx)
D. Une loi linéaire

64. Quand la fréquence augmente, l'atténuation :
A. S'annule
B. Augmente
C. Diminue
D. Reste constante

65. Quelle fréquence choisir pour explorer un organe profond ?
A. 20–50 MHz
B. Une haute fréquence
C. Peu importe
D. Une basse fréquence

66. Le gain général :
A. Remplace la TGC
B. Agit sur toute l'image
C. N'existe qu'en Doppler
D. Agit sur une seule tranche de profondeur

67. TGC signifie :
A. Total Gain Control
B. Time Gain Compensation
C. Time Graph Calibration
D. Tissue Gradient Curve

68. L'objectif de la TGC est d'obtenir :
A. Une échogénicité homogène du haut en bas pour un même tissu
B. Une image entièrement noire
C. Une suppression du bruit
D. Un contraste maximal en haut seulement

69. La formule du coefficient de réflexion est :
A. R = Z₂ × Z₁
B. R = ((Z₂ − Z₁)/(Z₂ + Z₁))²
C. R = (Z₂ − Z₁)/c
D. R = (Z₂ + Z₁)/(Z₂ − Z₁)

70. Le coefficient de réflexion de l'interface tissu mou – air est d'environ :
A. 1 %
B. 99 %
C. 50 %
D. 30 %

71. Le coefficient de réflexion de l'interface tissu mou – os est d'environ :
A. 99 %
B. 0 %
C. 30 %
D. 1 %

72. Le coefficient de réflexion de l'interface rein – graisse est d'environ :
A. 30 %
B. 50 %
C. 1 %
D. 99 %

73. Le coefficient de réflexion R dépend :
A. De la fréquence uniquement
B. De la différence d'impédance, pas de la fréquence
C. De la durée de l'examen
D. De la couleur de l'image

74. Pour qu'une interface soit bien visible, le faisceau doit la frapper :
A. Toujours très obliquement
B. À 10° quelconque
C. Tangentiellement
D. Perpendiculairement

75. Pourquoi utilise-t-on un gel ?
A. Pour absorber les ultrasons
B. Pour refroidir la sonde
C. Pour chasser l'air entre la sonde et la peau
D. Pour augmenter la fréquence

76. Le Doppler transcrânien utilise :
A. L'os temporal comme fenêtre acoustique
B. L'os occipital épais uniquement
C. L'air des sinus
D. Une fenêtre sans os

77. La réfraction se produit :
A. En incidence perpendiculaire uniquement
B. Sur les cibles de 7 µm
C. En incidence oblique sur une interface séparant deux célérités différentes
D. Dans le vide

78. La loi de la réfraction s'écrit :
A. sin θi × sin θt = 1
B. θi = θt
C. c₁ = c₂
D. sin θi / sin θt = c₁ / c₂

79. La réfraction peut provoquer :
A. Une disparition du gel
B. Un décalage ou une distorsion de l'image (piège en ponction guidée)
C. Une absorption totale
D. Un renforcement du speckle

80. La diffraction survient :
A. En incidence perpendiculaire sur une paroi plane
B. Quand le faisceau est tangent aux bords d'une structure ronde ou tubulaire
C. Sur des cibles très petites
D. Dans le gel

81. Quelle est la conséquence de la diffraction ?
A. Une ombre acoustique latérale
B. Un échauffement
C. Une amplification du signal
D. Une homogénéisation du gain

82. La diffusion se produit sur :
A. Des interfaces plus grandes que λ
B. L'os uniquement
C. L'air uniquement
D. Des cibles plus petites que λ

83. Quelle est la taille approximative d'un globule rouge ?
A. ≈ 7 mm
B. ≈ 7 µm
C. ≈ 7 cm
D. ≈ 70 µm

84. Que produisent les globules rouges sous ultrasons ?
A. Une absorption totale
B. Une réflexion spéculaire intense
C. Une diffusion à l'origine du signal Doppler
D. Une réfraction

85. Le « speckle » est :
A. Un défaut de la sonde
B. Une photo exacte des cellules
C. Un bruit d'interférence dû aux nombreux diffuseurs
D. Un artefact de gel

86. La diffusion, quand la fréquence augmente :
A. Disparaît
B. Augmente
C. Diminue
D. Reste identique

87. L'intensité diffusée est en général :
A. Nulle
B. Identique
C. Plus faible que l'intensité réfléchie
D. Plus forte que l'intensité réfléchie

88. Un écho de réflexion spéculaire :
A. Est identique pour toute incidence
B. Est indépendant de l'angle
C. Correspond au speckle
D. Dépend de l'angle : visible si l'incidence est perpendiculaire

89. Le speckle :
A. A le même aspect quelle que soit l'incidence
B. Est toujours plus clair que les contours
C. Ne se voit que sur les parois
D. Disparaît si on incline la sonde

90. Parmi ces structures, laquelle donne surtout un écho spéculaire ?
A. Le parenchyme hépatique
B. Le sang circulant
C. La capsule d'un organe
D. Le parenchyme rénal

91. Parmi ces structures, laquelle donne surtout un écho de diffusion ?
A. Le diaphragme
B. La paroi vasculaire
C. La capsule de la rate
D. Le parenchyme hépatique

92. Vous inclinez la sonde : que se passe-t-il ?
A. Tout disparaît
B. Les contours peuvent changer, pas la texture
C. Rien ne change jamais
D. La texture disparaît, pas les contours

93. Quels mécanismes participent à l'absorption ?
A. Uniquement la réfraction
B. Uniquement la diffraction
C. Viscosité du milieu, phénomènes chimiques, cavitation
D. Uniquement la réflexion

94. La cavitation correspond à :
A. Un défaut du cristal
B. La formation / oscillation de microbulles sous l'effet de la pression
C. Un reflet sur l'os
D. Une perte de gel

95. Quel effet chimique est favorisé par les ultrasons ?
A. La fibrinolyse
B. La photosynthèse
C. La coagulation totale
D. La synthèse de collagène

96. Quelle conséquence de l'absorption fonde les règles de sécurité ?
A. La polarisation du cristal
B. La diffusion
C. L'échauffement des tissus
D. La perte de gel

97. En ponction échoguidée, il faut :
A. Garder l'extrémité de l'aiguille ET la cible sur l'image
B. Regarder seulement l'aiguille
C. Ne regarder que la cible
D. Fermer les yeux et se fier au calcul

98. Pourquoi une sonde basse fréquence en cardiologie ?
A. Pour supprimer l'atténuation
B. Pour la profondeur et franchir l'os (côtes, os temporal en Doppler transcrânien)
C. Pour augmenter la diffusion
D. Pour avoir une résolution maximale

99. Lequel de ces phénomènes dépend de la fréquence ?
A. La célérité à 1540 m/s dans tous les cas
B. La loi sin θi / sin θt = c₁/c₂
C. Le coefficient de réflexion R
D. L'atténuation

100. Pourquoi les gaz digestifs et le poumon gênent-ils l'examen ?
A. Absorption totale par l'eau
B. Diffusion sur les globules rouges
C. Z identique à celle des tissus
D. Interface tissu – air : réflexion ≈ 99 %

Corrigé commenté
Compte tes bonnes réponses : 90+ = excellent, 70–89 = bien, en dessous : relis la fiche de révision.
N° Rép. N° Rép. N° Rép. N° Rép. N° Rép.
1 C 21 A 41 A 61 A 81 A
2 D 22 B 42 C 62 B 82 D
3 B 23 D 43 C 63 C 83 B
4 C 24 B 44 C 64 B 84 C
5 B 25 B 45 A 65 D 85 C
6 C 26 D 46 D 66 B 86 B
7 D 27 A 47 A 67 B 87 C
8 B 28 A 48 B 68 A 88 D
9 C 29 D 49 A 69 B 89 A
10 D 30 B 50 A 70 B 90 C
11 B 31 D 51 D 71 C 91 D
12 D 32 A 52 D 72 C 92 B
13 C 33 C 53 B 73 B 93 C
14 C 34 C 54 D 74 D 94 B
15 D 35 C 55 D 75 C 95 A
16 A 36 C 56 D 76 A 96 C
17 A 37 A 57 D 77 C 97 A
18 B 38 D 58 A 78 D 98 B
19 B 39 A 59 A 79 B 99 D
20 D 40 A 60 C 80 B 100 D

Explications
N° Rép. Explication
1 C Non vulnérante : pas de rayonnement ionisant ni de contraste dans les conditions usuelles.
2 D Elle est à la fois morphologique et fonctionnelle.
3 B Précautions particulières chez la femme enceinte (ou susceptible de l'être) et pour l'œil.
4 C Chaque curseur règle une tranche de profondeur.
5 B Le son Doppler vient des haut-parleurs.
6 C Le signal Doppler est audible.
7 D Chaque mode a son gain ; les TGC corrigent selon la profondeur.
8 B La qualité de l'examen dépend de l'échographiste.
9 C Effet réversible.
10 D Tension → déformation → vibration = onde ultrasonore.
11 B Écho (pression) → déformation → tension.
12 D La sonde émet brièvement, puis « écoute ».
13 C Aucun champ électrique résultant.
14 C Les ions se déplacent, les centres de gravité se séparent.
15 D Il raccourcit l'impulsion.
16 A Ordre arrière → avant : amortisseur, électrode, cristal, électrode, lame, gel.
17 A Transition entre Z cristal (élevée) et Z tissus (≈ 1,5 MRayl).
18 B Voir schéma d'anatomie d'une sonde.
19 B Une électrode de chaque côté du cristal.
20 D Valeur de référence pour l'adaptation de la sonde.
21 A Coefficient de couplage électro-acoustique : plus il est élevé, mieux c'est.
22 B Fort couplage + bonne transmission.
23 D Matériau historique et naturel.
24 B Génération qui corrige le défaut du quartz.
25 B Chaque génération corrige un défaut de la précédente.
26 D Bon compromis entre céramiques et polymères.
27 A Capacitive Micromachined Ultrasonic Transducers.
28 A Des « micro-tambours » électrostatiques gravés sur silicium.
29 D Technologie plus récente, tournée vers l'avenir.
30 B Vibration mécanique qui se propage dans la matière.
31 D Pas de propagation dans le vide.
32 A Comme la « ola » d'un stade.
33 C 330 m/s correspond à l'air.
34 C c : célérité ; f : fréquence.
35 C Durée d'un cycle.
36 C 1540 / 10⁶ = 1,54 mm.
37 A 1540 / (10⁷) = 0,154 mm.
38 D Fréquence ↑ → λ ↓ → détails plus fins.
39 A Moins de 20 Hz : infrasons ; 20 Hz–20 kHz : audible ; > 200 MHz : hypersons.
40 A Jusqu'à 50 MHz en applications spécialisées.
41 A Très haute fréquence = très faible profondeur.
42 C Grande profondeur, résolution moindre.
43 C Profondeur et résolution moyennes.
44 C Faible profondeur, très bonne résolution.
45 A Un peu plus élevée que pour l'abdomen.
46 D Compromis fréquence / profondeur.
47 A Échelle logarithmique pour des intensités très étendues.
48 B +3 dB ≈ ×2 ; +10 dB = ×10 ; +20 dB = ×100.
49 A Repère à connaître.
50 A Deux fois +10 dB.
51 D Référence de l'échelle en dB.
52 D Elle mesure l'écart entre l'écho le plus fort et le plus faible traités.
53 B ρ : densité ; c : célérité.
54 D Très faible, d'où l'impédance très faible de l'air.
55 D Valeur moyenne dans les tissus mous.
56 D Tendon et os : impédance très élevée.
57 D Graisse : impédance faible.
58 A Elle dessine les contours des organes.
59 A Réflexion = contours.
60 C Diffusion sur les cibles plus petites que λ.
61 A Absorption = chaleur ; fonde les règles de sécurité.
62 B Elle augmente avec la profondeur et la fréquence.
63 C Décroissance exponentielle.
64 B Dans les tissus mous, elle est grossièrement proportionnelle à f.
65 D Moins d'atténuation, donc plus de profondeur.
66 B Les TGC agissent par tranche de profondeur.
67 B Compensation du gain en profondeur.
68 A On amplifie davantage les échos profonds.
69 B Plus l'écart de Z est grand, plus l'écho est fort.
70 B Rien ne passe derrière : gel obligatoire.
71 C Les structures derrière l'os sont masquées.
72 C Écho visible et énergie transmise pour voir plus loin.
73 B R ne dépend pas de f.
74 D La réflexion spéculaire dépend de l'angle.
75 C Air = réflexion ≈ 99 %.
76 A Exception à l'obstacle osseux.
77 C Loi de Snell-Descartes.
78 D Relation entre angles et célérités.
79 B Garder aiguille et cible visibles.
80 B Perte d'information : ombre acoustique latérale.
81 A Au bord d'un kyste ou d'un vaisseau.
82 D L'énergie est renvoyée dans toutes les directions.
83 B Très inférieure à λ (0,1–1 mm) : diffusion.
84 C Sang en Doppler.
85 C Grain gris des parenchymes.
86 B La diffusion augmente avec la fréquence.
87 C Les échos de diffusion sont plus faibles.
88 D Ligne nette (contour, paroi).
89 A Fines granulations grises.
90 C Capsule, paroi vasculaire, diaphragme.
91 D Parenchymes hépatique, splénique, rénal ; sang.
92 B Contours = réflexion ; texture = diffusion.
93 C Quel que soit le mécanisme, résultat final : échauffement.
94 B Mécanisme d'absorption.
95 A Exemple cité dans le cours.
96 C Intensité et durée d'exposition sont réglementées.
97 A Correction de trajectoire « à vue ».
98 B Basse fréquence = grande profondeur, résolution moindre.
99 D R dépend seulement de Z.
100 D Rien ne passe derrière l'air.
""".trimIndent()

    fun getDocument(): QcmDocument {
        val questions = QcmParser.parse(courseRawText)
        return QcmDocument(
            id = 1001L,
            title = "100 QCM : Généralités sur l'ultrasonographie",
            questions = questions,
            bestScore = null,
            createdAt = System.currentTimeMillis()
        )
    }
}
