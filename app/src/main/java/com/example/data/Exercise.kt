package com.example.data

data class Exercise(
    val id: String,
    val name: String,
    val category: String, // "Pernas", "Peito", "Costas", "Ombros", "Glúteos", "Abdominais", "Bíceps", "Tríceps", "Mobilidade"
    val primaryMuscle: String,
    val secondaryMuscles: String,
    val equipmentNeeded: String,
    val difficultyLevel: String, // "Iniciante", "Intermediário", "Avançado"
    val recommendedObjective: String,
    val executionSteps: List<String>,
    val commonErrors: List<String>,
    val postureTips: List<String>,
    val smartSubstitutions: List<String>,
    val easierVariation: String,
    val harderVariation: String,
    val safetyWarnings: List<String>,
    val tags: List<String>,
    val primaryFocus: String,
    val iconDescription: String
)

object CuratedExercises {
    val list = listOf(
        Exercise(
            id = "agachamento",
            name = "Agachamento Livre",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais, Eretores da Espinha",
            equipmentNeeded = "Nenhum ou Peso Corporal",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Hipertrofia & Força de Pernas",
            executionSteps = listOf(
                "Posicione os pés na largura dos ombros, apontando levemente para fora (cerca de 15 graus).",
                "Inicie o movimento empurrando o quadril para trás, flexionando os joelhos, como se fosse sentar em um banco.",
                "Desça o quadril de forma controlada até que as coxas fiquem pelo menos paralelas ao solo.",
                "Mantenha o peito aberto, abdômen contraído (bracing) e calcanhares firmes no chão.",
                "Empurre o solo com os pés para retornar à posição inicial, estendendo quadril e joelhos."
            ),
            commonErrors = listOf(
                "Deixar os joelhos entrarem em valgo (apontando para dentro).",
                "Tirar os calcanhares do chão ou desabar o tronco à frente.",
                "Curvar a coluna lombar na descida máxima ('butt wink')."
            ),
            postureTips = listOf(
                "Mantenha os joelhos sempre apontando na mesma direção dos dedos dos pés.",
                "Imagine que está empurrando o chão para longe em vez de apenas levantar o peso."
            ),
            smartSubstitutions = listOf(
                "Agachamento Goblet (com halter)",
                "Leg Press 45"
            ),
            easierVariation = "Agachamento na parede (Wall Sit) ou Agachamento com Apoio de Banco",
            harderVariation = "Agachamento Traseiro com Barra ou Agachamento Búlgaro",
            safetyWarnings = listOf(
                "Se tiver dores prévias nos joelhos, limite a descida até 90 graus.",
                "Não trave os joelhos de forma brusca no topo."
            ),
            tags = listOf("casa", "academia", "pernas", "iniciante", "coxa", "peso corporal"),
            primaryFocus = "Massa Muscular & Força",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "agachamento_goblet",
            name = "Agachamento Goblet",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais, Core, Eretores da Espinha",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Hipertrofia e Força Geral",
            executionSteps = listOf(
                "Segure um halter de forma vertical rente ao peito pelas extremidades.",
                "Realize o movimento de agachamento mantendo os cotovelos apontados para baixo.",
                "Desça até os cotovelos quase tocarem a parte interna dos joelhos.",
                "Empurre o calcanhar contra o chão e suba esmagando as coxas."
            ),
            commonErrors = listOf(
                "Afastar o halter do peito, jogando peso sobre a lombar.",
                "Arredondar os ombros para a frente na descida."
            ),
            postureTips = listOf(
                "Mantenha as escápulas aduzidas e o tronco o mais vertical possível para poupar a lombar."
            ),
            smartSubstitutions = listOf(
                "Agachamento Livre",
                "Leg Press"
            ),
            easierVariation = "Agachamento na parede (Wall Sit)",
            harderVariation = "Agachamento Búlgaro",
            safetyWarnings = listOf(
                "Se tiver dor ou desconforto lombar anterior, evite agachamentos com barra e use esta variação Goblet com halter."
            ),
            tags = listOf("casa", "academia", "pernas", "halteres", "iniciante", "lombar"),
            primaryFocus = "Hipertrofia & Força",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "leg_press",
            name = "Leg Press 45",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais, Panturrilhas",
            equipmentNeeded = "Máquina",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Hipertrofia Frontal de Coxa",
            executionSteps = listOf(
                "Sente-se no aparelho e posicione os pés na plataforma na largura dos ombros.",
                "Remova as travas de segurança com cuidado.",
                "Flexione os joelhos trazendo o peso em direção ao peito com máximo controle.",
                "Empurre o peso de volta, focando o esforço nos calcanhares."
            ),
            commonErrors = listOf(
                "Permitir que a lombar descole do encosto pélvico no final da descida.",
                "Estender totalmente os joelhos no topo (estalo articular perigoso)."
            ),
            postureTips = listOf(
                "Mantenha a cabeça relaxada no encosto e segure firme nas manoplas de segurança laterais."
            ),
            smartSubstitutions = listOf(
                "Agachamento Goblet",
                "Cadeira Extensora"
            ),
            easierVariation = "Cadeira Extensora",
            harderVariation = "Agachamento Búlgaro",
            safetyWarnings = listOf(
                "Se tiver dor lombar, diminua a amplitude antes que sua pelve comece a girar ou descolar do banco."
            ),
            tags = listOf("academia", "pernas", "maquina", "intermediario", "coxa"),
            primaryFocus = "Hipertrofia Máxima",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "bulgaro",
            name = "Agachamento Búlgaro",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais, Core, Estabilizadores",
            equipmentNeeded = "Halteres ou Peso Corporal",
            difficultyLevel = "Avançado",
            recommendedObjective = "Correção de Assimetrias Bilaterais",
            executionSteps = listOf(
                "Posicione a ponta de um pé para trás em cima de um banco rígido.",
                "Dê um passo largo à frente com a outra perna, segurando halteres ao lado do corpo.",
                "Desça o quadril verticalmente até que o joelho de trás esteja próximo ao chão.",
                "Empurre o solo com a sola do pé da frente para retornar."
            ),
            commonErrors = listOf(
                "Deixar o joelho da frente passar excessivamente a ponta do pé de forma instável.",
                "Tirar o calcanhar dianteiro do chão devido a passo muito curto."
            ),
            postureTips = listOf(
                "Incline ligeiramente o tronco à frente para trabalhar mais glúteos de forma segura."
            ),
            smartSubstitutions = listOf(
                "Agachamento Goblet",
                "Leg Press 45"
            ),
            easierVariation = "Avanço Passada ou Agachamento Goblet",
            harderVariation = "Agachamento Búlgaro com salto pliométrico",
            safetyWarnings = listOf(
                "Use cargas leves no início. Exige excelente controle de equilíbrio proprioceptivo."
            ),
            tags = listOf("academia", "casa", "pernas", "halteres", "avancado", "unilateral"),
            primaryFocus = "Força & Assimetrias",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "extensora",
            name = "Cadeira Extensora",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Nenhum (Isolamento)",
            equipmentNeeded = "Máquina",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Isolamento de Quadríceps",
            executionSteps = listOf(
                "Sente-se na cadeira garantindo que as costas estejam totalmente apoiadas.",
                "Ajuste o rolo de espuma logo acima da flexão dos seus tornozelos.",
                "Estenda as pernas contraindo as coxas até o topo com controle total.",
                "Desça controlando a carga concentricamente sem despencar."
            ),
            commonErrors = listOf(
                "Chutar o peso de forma explosiva sem controlar a fase excêntrica da descida.",
                "Não alinhar o joelho com o eixo de rotação da máquina."
            ),
            postureTips = listOf(
                "Mantenha o quadril pressionado contra o assento usando as alças laterais."
            ),
            smartSubstitutions = listOf(
                "Agachamento Goblet",
                "Agachamento na Parede (Wall Sit)"
            ),
            easierVariation = "Extensão de Pernas sentada com elástico",
            harderVariation = "Agachamento Búlgaro",
            safetyWarnings = listOf(
                "Se tiver dor patelar crônica, evite estender com carga excessiva no topo (limite o esforço)."
            ),
            tags = listOf("academia", "pernas", "maquina", "iniciante", "joelho"),
            primaryFocus = "Hipertrofia de Isolamento",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "wall_sit",
            name = "Agachamento na Parede (Wall Sit)",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais",
            equipmentNeeded = "Nenhum (Parede)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Proteção de Joelho / Isometria Terapêutica",
            executionSteps = listOf(
                "Encoste as costas completamente contra a parede de forma lisa.",
                "Desça as costas deslizando até que seus joelhos e quadris atinjam 90 graus.",
                "Mantenha os pés firmes, canelas verticais e mãos livres ao lado.",
                "Sustente a posição respirando ritmadamente pelo tempo prescrito."
            ),
            commonErrors = listOf(
                "Apoiar as mãos sobre as coxas para aliviar a carga das pernas.",
                "Deixar os calcanhares saírem do chão."
            ),
            postureTips = listOf(
                "Mantenha toda a extensão da coluna lombar atada de forma estável à parede."
            ),
            smartSubstitutions = listOf(
                "Ponte de Glúteos no Solo",
                "Cadeira Extensora lenta"
            ),
            easierVariation = "Wall Sit em ângulo de 45 graus (altura mais alta)",
            harderVariation = "Wall Sit segurando um halter ou unilateral",
            safetyWarnings = listOf(
                "Excelente exercício clínico para condromalácia e tendinite patelar sem impacto mecânico."
            ),
            tags = listOf("casa", "pernas", "peso corporal", "iniciante", "joelho", "reabilitacao"),
            primaryFocus = "Resistência & Pró-Articulação",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "rdl",
            name = "Levantamento Terra RDL (Stiff)",
            category = "Pernas",
            primaryMuscle = "Isquiotibiais (Posterior de Coxa)",
            secondaryMuscles = "Glúteo Máximo, Eretores da Espinha, Core",
            equipmentNeeded = "Halteres ou Barra",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Hipertrofia da Cadeia Posterior & Postura",
            executionSteps = listOf(
                "Fique de pé com os pés na largura do quadril, segurando as cargas rente às coxas.",
                "Destrave ligeiramente os joelhos (mantenha-os flexionados em cerca de 10-15 graus fixos).",
                "Inicie descendo o tronco empurrando o quadril o máximo possível para trás, sentindo alongar os posteriores.",
                "Desça o peso raspando as pernas até a linha média das canelas, com a coluna perfeitamente neutra.",
                "Retorne contraindo fortemente os glúteos e empurrando o quadril para frente."
            ),
            commonErrors = listOf(
                "Arredondar a coluna lombar ou cervical de qualquer forma.",
                "Flexionar excessivamente ou esticar totalmente os joelhos durante a descida.",
                "Afastar os pesos do corpo, sobrecarregando a lombar."
            ),
            postureTips = listOf(
                "Imagine que precisa empurrar uma porta com o bumbum atrás de você.",
                "Mantenha o pescoço alinhado com a coluna: olhe para baixo na descida e à frente no topo."
            ),
            smartSubstitutions = listOf(
                "Cadeira Flexora na polia/máquina",
                "Elevação Pélvica (Hip Thrust)"
            ),
            easierVariation = "Bom Dia (Good Morning) com Peso Corporal ou Ponte de Glúteos no Solo",
            harderVariation = "Levantamento Terra Convencional ou RDL Unilateral (Single Leg RDL)",
            safetyWarnings = listOf(
                "ATENÇÃO: Evite ou modifique substancialmente se relatar dores agudas na Lombar.",
                "Mantenha o abdômen contraído o tempo inteiro para proteger a fáscia lombar."
            ),
            tags = listOf("academia", "pernas", "halteres", "barra", "lombar", "posterior"),
            primaryFocus = "Cadeia Posterior & Postura",
            iconDescription = "Posterior"
        ),
        Exercise(
            id = "elevacao_pelvica",
            name = "Elevação Pélvica (Hip Thrust)",
            category = "Glúteos",
            primaryMuscle = "Glúteo Máximo",
            secondaryMuscles = "Isquiotibiais, Quadríceps, Core, Adutores",
            equipmentNeeded = "Halter ou Barra Acolchoada",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Força de Quadril & Hipertrofia de Glúteos",
            executionSteps = listOf(
                "Apoie a porção média das costas (escápulas) de forma confortável no banco.",
                "Posicione os pés de modo que, ao subir o quadril, os joelhos fiquem em 90 graus exatos.",
                "Coloque a carga (halter ou barra) confortavelmente logo acima do quadril pélvico.",
                "Desça o quadril olhando levemente à frente para manter o pescoço alinhado.",
                "Empurre fortemente o solo com os calcanhares para erguer a pelve até o alinhamento com o tronco."
            ),
            commonErrors = listOf(
                "Arquear excessivamente a coluna lombar no topo.",
                "Olhar para o teto durante todo o movimento desnecessariamente.",
                "Subir empurrando com as pontas dos pés, ativando menos glúteos."
            ),
            postureTips = listOf(
                "Mantenha o queixo direcionado levemente ao peito no início da descida; imagine travar uma folha de papel com o queixo.",
                "Suba encolhendo os glúteos no topo para ativar a contração isolada máxima."
            ),
            smartSubstitutions = listOf(
                "Ponte de Glúteos no solo (com elástico ou halter menor)",
                "Agachamento Sumo com halter"
            ),
            easierVariation = "Ponte Pélvica Simples no Solo (sem peso)",
            harderVariation = "Elevação Pélvica Unilateral",
            safetyWarnings = listOf(
                "Mantenha o abdômen contraído o tempo inteiro; o esforço deve vir da bacia e calcanhares.",
                "Apoie-se em banco estável e firme contra contra-recuos."
            ),
            tags = listOf("casa", "academia", "glúteos", "halteres", "barra", "iniciante", "joelho"),
            primaryFocus = "Força de Quadril & Estética",
            iconDescription = "Glúteos"
        ),
        Exercise(
            id = "ponte_solo",
            name = "Ponte de Glúteos no Solo",
            category = "Glúteos",
            primaryMuscle = "Glúteo Máximo",
            secondaryMuscles = "Isquiotibiais, Core, Lombar",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Estabilização Lombar e Ativação de Glúteo",
            executionSteps = listOf(
                "Deite de costas no solo com os joelhos flexionados e pés apoiados próximos ao bumbum.",
                "Mantenha os braços ao longo do corpo com as palmas das mãos voltadas para baixo.",
                "Suba o quadril empurrando com os calcanhares até o corpo formar uma linha dos ombros aos joelhos.",
                "Retorne encostando o bumbum de forma suave no colchonete."
            ),
            commonErrors = listOf(
                "Afastar muito os pés de modo que use apenas a coxa posterior.",
                "Não tensionar o bumbum no ponto máximo."
            ),
            postureTips = listOf(
                "Ideal para quem sente dores ou fisgadas lombares constantes nos agachamentos."
            ),
            smartSubstitutions = listOf(
                "Elevação Pélvica",
                "Perdigueiro (Bird Dog)"
            ),
            easierVariation = "Ponte simples sem sobrecarga",
            harderVariation = "Elevação Pélvica com Halter ou Unilateral no Solo",
            safetyWarnings = listOf(
                "Ativa fortemente o core lombar posterior de forma terapêutica clínica."
            ),
            tags = listOf("casa", "glúteos", "peso corporal", "iniciante", "lombar"),
            primaryFocus = "Ativação & Alívio",
            iconDescription = "Glúteos"
        ),
        Exercise(
            id = "supino_halter",
            name = "Supino Reto com Halteres",
            category = "Peito",
            primaryMuscle = "Peitoral Maior",
            secondaryMuscles = "Deltoide Anterior, Tríceps Braquial",
            equipmentNeeded = "Banco e Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Hipertrofia & Força de Empurrão",
            executionSteps = listOf(
                "Deite-se no banco plano mantendo os quatro apoios: cabeça, ombros, glúteos elevados e pés firmes no chão.",
                "Posicione os halteres lateralmente ao peito, com os cotovelos dobrados em cerca de 75 a 90 graus.",
                "Empurre os pesos verticalmente em um arco suave até estender os braços, sem chocar os halteres.",
                "Desça controlando a carga até os halteres tocarem suavemente a linha do peito lateral."
            ),
            commonErrors = listOf(
                "Abrir os cotovelos a 90 graus completos, gerando pinçamento no ombro.",
                "Esticar demais os cotovelos gerando impacto articular brusco no topo.",
                "Tirar os pés do chão durante o esforço pesado de empurrão."
            ),
            postureTips = listOf(
                "Mantenha as escápulas aduzidas (pressione os ombros para trás e para baixo contra o banco) para proteger os ombros.",
                "Mantenha o punho perfeitamente alinhado verticalmente sobre o cotovelo."
            ),
            smartSubstitutions = listOf(
                "Flexão de Braço no solo (Push Up)",
                "Supino Inclinado com Halteres"
            ),
            easierVariation = "Flexão de Braço com Joelhos Apoiados ou Supino com Halteres no Chão (Floor Press)",
            harderVariation = "Supino com Barra ou Supino Declinado/Inclinado",
            safetyWarnings = listOf(
                "Evite se tiver lesões ou sensibilidade extrema no Ombro.",
                "Peça auxílio de um parceiro se usar cargas máximas para evitar incidentes."
            ),
            tags = listOf("academia", "peito", "halteres", "intermediário", "ombro"),
            primaryFocus = "Hipertrofia & Força de Empurrar",
            iconDescription = "Peito"
        ),
        Exercise(
            id = "supino_inclinado_halter",
            name = "Supino Inclinado com Halteres",
            category = "Peito",
            primaryMuscle = "Peitoral Superior (Porção Clavicular)",
            secondaryMuscles = "Deltoide Anterior, Tríceps Braquial",
            equipmentNeeded = "Banco Inclinado e Halteres",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Foco em Porção Superior e Estética de Ombros",
            executionSteps = listOf(
                "Ajuste um banco para inclinação média entre 30 e 40 graus.",
                "Deite de forma estável mantendo as escápulas bem encaixadas contra o banco.",
                "Empurre os halteres verticalmente acima da porção superior do tórax.",
                "Retorne flexionando os cotovelos em sentido diagonal e seguro."
            ),
            commonErrors = listOf(
                "Inclinar o banco a 60 graus ou mais, anulando o peito e usando apenas o deltoide frontal.",
                "Bater os halteres com estardalhaço no topo diminuindo a tensão sob controle."
            ),
            postureTips = listOf(
                "Trave a musculatura dorsal retraindo os ombros antes de tirar a carga para iniciar e finalizar."
            ),
            smartSubstitutions = listOf(
                "Supino Reto com Halteres",
                "Flexão de Braço Inclinada na Parede/Banco"
            ),
            easierVariation = "Flexão de peito em banco inclinado",
            harderVariation = "Crucifixo Inclinado com Halteres",
            safetyWarnings = listOf(
                "Excelente aliado para quem quer definir o peito poupando a porção inferior de estresses bruscos."
            ),
            tags = listOf("academia", "peito", "halteres", "intermediario", "ombro"),
            primaryFocus = "Peitoral Superior",
            iconDescription = "Peito"
        ),
        Exercise(
            id = "flexao_braço",
            name = "Flexão de Braço (Push Up)",
            category = "Peito",
            primaryMuscle = "Peitoral Maior",
            secondaryMuscles = "Tríceps, Deltoide Anterior, Reto Abdominal (Core)",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Força Básica de Empurrar e Resistência",
            executionSteps = listOf(
                "Adote a posição de flexão colocando as mãos ligeiramente mais abertas que os ombros.",
                "Mantenha o corpo linear como uma prancha sólida, contraindo bumbum e abdômen.",
                "Desça flexionando os braços fazendo o peito quase tocar o chão.",
                "Empurre o chão de volta estendendo os braços com estabilidade."
            ),
            commonErrors = listOf(
                "Deixar o quadril desabar caindo em direção ao chão (sela pélvica).",
                "Direcionar os cotovelos totalmente abertos imitando asas em 90 graus."
            ),
            postureTips = listOf(
                "Pense em empurrar o chão para longe, mantendo o pescoço posicionado de forma neutra."
            ),
            smartSubstitutions = listOf(
                "Supino Reto com Halteres",
                "Supino no Chão (Floor Press)"
            ),
            easierVariation = "Flexão de Braço com Joelhos Apoiados ou na Inclinação da Parede",
            harderVariation = "Flexão Diamante ou Flexão Declinada (pés elevados)",
            safetyWarnings = listOf(
                "Se houver incômodo nos ombros, limite a descida até as escápulas se alinharem ou use as variações mais fáceis."
            ),
            tags = listOf("casa", "peito", "peso corporal", "iniciante", "core"),
            primaryFocus = "Resistência de Empurre",
            iconDescription = "Peito"
        ),
        Exercise(
            id = "pull_up",
            name = "Barra Fixa (Pull Up)",
            category = "Costas",
            primaryMuscle = "Latíssimo do Dorso (Dorsais)",
            secondaryMuscles = "Bíceps, Romboides, Redondo Maior, Core",
            equipmentNeeded = "Nenhum (Barra Estática)",
            difficultyLevel = "Avançado",
            recommendedObjective = "Largura de Dorsais e Força de Tração",
            executionSteps = listOf(
                "Pendure-se em uma barra horizontal usando pegada pronada ligeiramente aberta.",
                "Inicie puxando-se para cima ativando as costas, mandando os cotovelos em direção às costelas.",
                "Puxe até o queixo ultrapassar de forma controlada a linha da barra.",
                "Desça retornando de forma lenta até que seus braços estejam esticados."
            ),
            commonErrors = listOf(
                "Dar impulsos violentos com as pernas ou pular para alcançar (kipping/roubo).",
                "Curvar os ombros à frente no pico de contração."
            ),
            postureTips = listOf(
                "Mantenha o abdômen tensionado e cruze levemente as canelas atrás para estabilidade máxima."
            ),
            smartSubstitutions = listOf(
                "Puxada Alta na Polia",
                "Remada Curvada com Halteres"
            ),
            easierVariation = "Barra assistida com elástico (superband) ou Puxada Alta",
            harderVariation = "Barra Fixa com Carga Adicional (Cinto de sobrecarga)",
            safetyWarnings = listOf(
                "Exige força de tração excelente. Evite se sentir cansaço ou dor no manguito rotador articular."
            ),
            tags = listOf("academia", "casa", "costas", "peso corporal", "avancado"),
            primaryFocus = "Costas Largas & Força",
            iconDescription = "Costas"
        ),
        Exercise(
            id = "puxada_alta",
            name = "Puxada Alta na Polia",
            category = "Costas",
            primaryMuscle = "Latíssimo do Dorso",
            secondaryMuscles = "Bíceps, Trapézio, Redondo Maior",
            equipmentNeeded = "Polia / Máquina",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Fortalecimento Geral de Puxadas",
            executionSteps = listOf(
                "Ajuste o rolo de espuma sobre as coxas para não levantar durante a puxada.",
                "Segure a barra com pegada pronada aberta e sente-se de forma ereta.",
                "Puxe a barra em direção ao peitoral superior estufando o peito à frente.",
                "Retorne alongando as costas controlando a subida."
            ),
            commonErrors = listOf(
                "Puxar a barra por trás do pescoço (coloca o ombro em grave estresse mecânico).",
                "Desabar e curvar a coluna lombar para trás no momento da puxada."
            ),
            postureTips = listOf(
                "Pense em trazer os cotovelos para baixo, esmagando a asa lateral das costas."
            ),
            smartSubstitutions = listOf(
                "Barra Fixa",
                "Remada Curvada com Halteres"
            ),
            easierVariation = "Puxada com elástico na polia em pé",
            harderVariation = "Barra Fixa assistida ou livre",
            safetyWarnings = listOf(
                "Variação extremamente segura de tração vertical para quem tem lombar fraca ou sensível."
            ),
            tags = listOf("academia", "costas", "maquina", "iniciante"),
            primaryFocus = "Dorsal Largura",
            iconDescription = "Costas"
        ),
        Exercise(
            id = "remada_curvada",
            name = "Remada Curvada com Halteres",
            category = "Costas",
            primaryMuscle = "Latíssimo do Dorso (Dorsal)",
            secondaryMuscles = "Trapézio, Romboides, Deltoide Posterior, Bíceps Braquial",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Postura & Largura de Costas",
            executionSteps = listOf(
                "Fique de pé segurando um halter em cada mão, curve o tronco à frente em cerca de 45-60 graus.",
                "Mantenha os joelhos levemente flexionados e a coluna lombar travada na posição neutra.",
                "Comece com os braços esticados para baixo.",
                "Puxe os halteres trazendo os cotovelos rente às costelas e direcionando-os em direção ao quadril.",
                "Esprema as escápulas no topo, segurando por 1 segundo, então desça controladamente."
            ),
            commonErrors = listOf(
                "Puxar verticalmente em direção ao peito com o cotovelo excessivamente aberto (carrega trapézio superior).",
                "Fazer movimentos bruscos com o tronco para embalar o peso.",
                "Curvar excessivamente a lombar por cansaço."
            ),
            postureTips = listOf(
                "Puxe imaginando que seus cotovelos têm que tocar um ao outro atrás das suas costas.",
                "Mantenha os ombros relaxados para longe das orelhas."
            ),
            smartSubstitutions = listOf(
                "Remada Sentada na Polia Baixa",
                "Remada Serrote Unilateral (com apoio no banco, mais seguro para a lombar)"
            ),
            easierVariation = "Remada com Toalha/Elástico em Pé ou Remada Invertida no TRX",
            harderVariation = "Remada Curvada com Barra (Pegada Pronada ou Supinada)",
            safetyWarnings = listOf(
                "Se a Lombar fadigar rápido, mude para a versão serrote unilateral com apoio no banco."
            ),
            tags = listOf("casa", "academia", "costas", "halteres", "trapezio", "postura"),
            primaryFocus = "Postura & Largura de Costas",
            iconDescription = "Costas"
        ),
        Exercise(
            id = "remada_serrote",
            name = "Remada Serrote Unilateral",
            category = "Costas",
            primaryMuscle = "Latíssimo do Dorso",
            secondaryMuscles = "Bíceps, Deltoide Posterior, Romboides",
            equipmentNeeded = "Halteres e Banco",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Controle Lateral de Dorsal e Isolamento Estável",
            executionSteps = listOf(
                "Apoie um joelho e a mão do mesmo lado em cima de um banco horizontal.",
                "Mantenha o tronco quase paralelo ao solo e segure o halter com a mão livre.",
                "Puxe o peso trazendo seu cotovelo rente à cintura em formato de meia-lua diagonal.",
                "Retorne estendendo até o alongamento confortável sem desalinhar os quadris."
            ),
            commonErrors = listOf(
                "Girar o tronco todo de lado para conseguir mover cargas inadequadas.",
                "Trazer o peso em linha puramente reta, ativando demais o bíceps e poupando dorsais."
            ),
            postureTips = listOf(
                "Estabilizar no banco apoia inteiramente a lombar, ideal para casos de hérnias e fadigas."
            ),
            smartSubstitutions = listOf(
                "Remada Curvada com Halteres",
                "Remada Sentada com Cabo"
            ),
            easierVariation = "Remada serrote em posição de passada livre no solo",
            harderVariation = "Remada Serrote livre sem apoio",
            safetyWarnings = listOf(
                "Opção predileta dos ortopedistas para hipertrofia dorsal em quem sofre de desgaste lombar articular."
            ),
            tags = listOf("academia", "casa", "costas", "halteres", "iniciante", "lombar"),
            primaryFocus = "Isolamento Estável",
            iconDescription = "Costas"
        ),
        Exercise(
            id = "desenvolvimento_sentado",
            name = "Desenvolvimento de Ombros",
            category = "Ombros",
            primaryMuscle = "Deltoide Anterior",
            secondaryMuscles = "Deltoide Lateral, Tríceps, Trapézio Superior",
            equipmentNeeded = "Banco e Halteres",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Estabilidade Vertical de Membros Superiores",
            executionSteps = listOf(
                "Sente-se firmemente no banco e alinhe os halteres na altura das orelhas.",
                "Dobre ligeiramente os cotovelos um pouco para a frente (direção do peito).",
                "Empurre os halteres para cima de forma sincronizada até estender quase tudo de forma vertical.",
                "Desça o peso de volta sentindo alongar os deltoides anteriores."
            ),
            commonErrors = listOf(
                "Arquear a lombar de forma lordótica compensando a carga com o peito.",
                "Apontar cotovelos em 90 graus retos correndo o risco de impacto articular agudo."
            ),
            postureTips = listOf(
                "Trabalhe na linha do plano escapular: cotovelos posicionados em cerca de 30 graus à frente."
            ),
            smartSubstitutions = listOf(
                "Elevação Lateral com Halteres",
                "Face Pull na Polia"
            ),
            easierVariation = "Desenvolvimento com pegada neutra ou menor carga sem encosto",
            harderVariation = "Desenvolvimento de pé (Overhead Press)",
            safetyWarnings = listOf(
                "Se relatar incômodo no manguito rotador, modifique para pegada neutra com palmas voltadas para dentro."
            ),
            tags = listOf("academia", "casa", "ombros", "halteres", "intermediario", "ombro"),
            primaryFocus = "Ombros Fortes & Estabilidade",
            iconDescription = "Ombros"
        ),
        Exercise(
            id = "elevacao_lateral",
            name = "Elevação Lateral com Halteres",
            category = "Ombros",
            primaryMuscle = "Deltoide Lateral",
            secondaryMuscles = "Deltoide Anterior, Trapézio",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Desenvolvimento Lateral e Ombros Esculpidos",
            executionSteps = listOf(
                "Fique de pé com halteres segurados ao lado das coxas.",
                "Incline levemente o tórax de 15 graus para a frente.",
                "Eleve os braços para os lados mantendo os cotovelos micro-flexionados em direção para fora.",
                "Desça freando o peso com as laterais do braço."
            ),
            commonErrors = listOf(
                "Fazer balanço corporal com a coluna para jogar carga alta para o alto.",
                "Esticar totalmente o cotovelo ou virar as palmas no final expondo a fáscia supraespinhal."
            ),
            postureTips = listOf(
                "Sempre eleve os halteres no plano escapular (levemente à frente do alinhamento ombro-ombro lateral)."
            ),
            smartSubstitutions = listOf(
                "Desenvolvimento com Halteres",
                "Elevação Lateral na Polia"
            ),
            easierVariation = "Elevação lateral sentada com polias leves",
            harderVariation = "Elevação Lateral com cabo unilateral na altura do quadril",
            safetyWarnings = listOf(
                "Excelente para criar aspecto atlético sem forçar os manguitos rotadores se executado no plano escapular."
            ),
            tags = listOf("casa", "academia", "ombros", "halteres", "iniciante"),
            primaryFocus = "Deltoide Lateral",
            iconDescription = "Ombros"
        ),
        Exercise(
            id = "face_pull",
            name = "Face Pull na Polia",
            category = "Ombros",
            primaryMuscle = "Deltoide Posterior",
            secondaryMuscles = "Manguito Rotador, Trapézio Médio/Superior, Romboides",
            equipmentNeeded = "Polia / Elástico",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Postura Tridimensional de Ombros e Reabilitação Cirúrgica",
            executionSteps = listOf(
                "Instale a corda em polia posicionada próximo à altura do rosto.",
                "Dê alguns passos para trás segurando nas extremidades da corda.",
                "Puxe em direção aos olhos ou testa, afastando as mãos de lado na contração máxima.",
                "Retorne alongando e sentindo a escápula se abrir."
            ),
            commonErrors = listOf(
                "Tirar os cotovelos da posição suspensa jogando peso para baixo.",
                "Usar força excessiva do bíceps sem tensionar a porção traseira de ombro."
            ),
            postureTips = listOf(
                "Execute uma rotação externa dos ombros no final (mãos terminam mais atrás que os cotovelos)."
            ),
            smartSubstitutions = listOf(
                "Crucifixo Invertido",
                "Remada Curvada pegada aberta"
            ),
            easierVariation = "Face pull usando elástico amarrado em quina de casa",
            harderVariation = "Face Pull em isometria mantida no topo por 3s",
            safetyWarnings = listOf(
                "Amplamente preconizado por fisioterapeutas esportivos para estabilização de manguito e eliminação de dores de impacto."
            ),
            tags = listOf("academia", "casa", "ombros", "polia", "iniciante", "postura", "reabilitacao"),
            primaryFocus = "Postura & Ombros Saudáveis",
            iconDescription = "Ombros"
        ),
        Exercise(
            id = "crucifixo_invertido",
            name = "Crucifixo Invertido",
            category = "Ombros",
            primaryMuscle = "Deltoide Posterior",
            secondaryMuscles = "Romboides, Trapézio, Redondo Menor",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Fortalecimento do Dorso Posterior para Alinhamento",
            executionSteps = listOf(
                "Curve o tronco de pé a 45 graus ou deite-se de barriga em banco ligeiramente inclinado a 30 graus.",
                "Deixe os halteres suspensos verticalmente.",
                "Com os braços quase retos, abra-os de lado elevando os halteres.",
                "Foque na contração da musculatura superior e traseira sem mola elástica."
            ),
            commonErrors = listOf(
                "Erguer o tronco de pé perdendo a inclinação para costas.",
                "Balançar os pesos à frente para criar momentum de carga."
            ),
            postureTips = listOf(
                "A versão deitada de peito no banco é ideal para aliviar todo e qualquer esforço de lombar."
            ),
            smartSubstitutions = listOf(
                "Face Pull na Polia",
                "Remada serrote focando cotovelos para fora"
            ),
            easierVariation = "Crucifixo Invertido sem carga (apenas ativação neuromuscular)",
            harderVariation = "Crucifixo Invertido com cabo unilateral na polia baixa",
            safetyWarnings = listOf(
                "Essencial para corrigir ombros fadigados que rotacionam para dentro por uso excessivo de escritório ou direção."
            ),
            tags = listOf("academia", "casa", "ombros", "halteres", "intermediario", "postura"),
            primaryFocus = "Postural Posterior",
            iconDescription = "Ombros"
        ),
        Exercise(
            id = "rosca_direta",
            name = "Rosca Direta com Halteres",
            category = "Bíceps",
            primaryMuscle = "Bíceps Braquial",
            secondaryMuscles = "Braquial, Antebraço",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Isolamento de Braços",
            executionSteps = listOf(
                "De pé mantendo os pés alinhados com o quadril.",
                "Segure as cargas com as palmas voltadas para a frente.",
                "Flexione os cotovelos sem movimentá-los para frente ou para trás.",
                "Contraia o bíceps com força máxima no topo de forma iscorotatória."
            ),
            commonErrors = listOf(
                "Alavancar a lombar para trás em arquear lordótico desnecessário.",
                "Estender com soltura abrupta a descida sem contração de freio mecânico."
            ),
            postureTips = listOf(
                "Mantenha o bíceps no plano de tensão: cotovelos sempre pregados nas laterais de suas costelas."
            ),
            smartSubstitutions = listOf(
                "Rosca Martelo",
                "Rosca Inversa"
            ),
            easierVariation = "Rosca Alternada sentada",
            harderVariation = "Rosca Concentrada com halter unilateral",
            safetyWarnings = listOf(
                "Pode ser feita sentada para maior contenção e segurança de força se você tem dores nas costas."
            ),
            tags = listOf("casa", "academia", "biceps", "halteres", "iniciante"),
            primaryFocus = "Bíceps Isolamento",
            iconDescription = "Bíceps"
        ),
        Exercise(
            id = "rosca_martelo",
            name = "Rosca Martelo",
            category = "Bíceps",
            primaryMuscle = "Braquiorradial",
            secondaryMuscles = "Bíceps Braquial, Braquial",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Definição de Antebraço e Pegada",
            executionSteps = listOf(
                "Fique em posição ereta segurando halteres com pegada neutra.",
                "Suba o peso verticalmente em movimento de martelar, mantendo as palmas para dentro.",
                "Controle o esforço até o ponto mais alto e retorne freando a descida."
            ),
            commonErrors = listOf(
                "Usar momentum e balançar o corpo no topo.",
                "Aliviar a descida despencando sem controle excêntrico."
            ),
            postureTips = listOf(
                "Excelente opção preventiva para punhos sensíveis que estalam na rosca direta convencional."
            ),
            smartSubstitutions = listOf(
                "Rosca Direta",
                "Rosca Concentrada"
            ),
            easierVariation = "Rosca martelo sentada de forma confortável",
            harderVariation = "Rosca Martelo na polia com cabo corda",
            safetyWarnings = listOf(
                "Estabiliza as articulações radiais de punho e cotovelo de forma notável."
            ),
            tags = listOf("casa", "academia", "biceps", "halteres", "iniciante"),
            primaryFocus = "Braço & Pegada",
            iconDescription = "Bíceps"
        ),
        Exercise(
            id = "triceps_corda",
            name = "Tríceps Corda na Polia",
            category = "Tríceps",
            primaryMuscle = "Tríceps Cabeça Lateral",
            secondaryMuscles = "Tríceps Cabeça Curta/Longa",
            equipmentNeeded = "Polia / Máquina",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Isolamento de Tríceps e Força Lateral",
            executionSteps = listOf(
                "Segure a corda ligada à extremidade da polia alta de frente para o aparelho.",
                "Cotovelos flexionados ligeiramente à frente do corpo.",
                "Estenda o braço para baixo estalando diagonalmente a corda para fora no final.",
                "Retorne controlando e sentindo alongar a porção traseira de seus braços."
            ),
            commonErrors = listOf(
                "Abrir os cotovelos nas extremidades ao final da força excessiva.",
                "Tirar os ombros do lugar de trava postural."
            ),
            postureTips = listOf(
                "Pense em esmagar a porção lateral do abdômen com suas soluçadas."
            ),
            smartSubstitutions = listOf(
                "Tríceps Testa",
                "Flexão Diamante"
            ),
            easierVariation = "Tríceps pulley com barra reta na segurança",
            harderVariation = "Tríceps Francês na polia ou halteres",
            safetyWarnings = listOf(
                "Excelente isolador de braços amigável para tendões se não carregar de forma agressiva."
            ),
            tags = listOf("academia", "triceps", "polia", "iniciante"),
            primaryFocus = "Definição de Braço",
            iconDescription = "Tríceps"
        ),
        Exercise(
            id = "triceps_frances_halter",
            name = "Tríceps Francês",
            category = "Tríceps",
            primaryMuscle = "Tríceps Cabeça Longa",
            secondaryMuscles = "Tríceps Cabeça Lateral, Core",
            equipmentNeeded = "Halteres",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Massa e Volume Traseiro de Braço",
            executionSteps = listOf(
                "Sente-se no banco, segure o cabo de um halter pesado com ambas as mãos em triângulo.",
                "Erga de forma vertical acima de sua cabeça.",
                "Dobre os cotovelos lentamente trazendo o halter por trás de sua nuca.",
                "Estenda os antebraços erguendo novamente de forma reta acima."
            ),
            commonErrors = listOf(
                "Abrir cotovelos de banda perdendo a tensão vertical sobre as cabeças do tríceps.",
                "Bater o halter na cervical por pura falta de controle lateral."
            ),
            postureTips = listOf(
                "Seus cotovelos devem apontar o máximo possível para o teto e não para as laterais."
            ),
            smartSubstitutions = listOf(
                "Tríceps Corda na Polia",
                "Tríceps Testa no banco"
            ),
            easierVariation = "Tríceps pressdown unilateral sem inclinações",
            harderVariation = "Tríceps Francês unilateral livre em pé",
            safetyWarnings = listOf(
                "Evite se você tem rigidez articular nos ombros e não consegue alongar sem arquear a coluna lombar."
            ),
            tags = listOf("academia", "casa", "triceps", "halteres", "intermediario"),
            primaryFocus = "Tríceps Cabeça Longa",
            iconDescription = "Tríceps"
        ),
        Exercise(
            id = "prancha",
            name = "Prancha Isométrica",
            category = "Abdominais",
            primaryMuscle = "Reto Abdominal",
            secondaryMuscles = "Glúteos, Oblíquos, Transverso do Abdômen, Core Posterior",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Estabilização de Coluna e Abdômen Blindado",
            executionSteps = listOf(
                "Apoie seus antebraços paralelos no solo na altura de seus cotovelos.",
                "Adote a suspensão corporal ficando com apoio apenas em antebraços e pontas de pés.",
                "Mantenha o corpo perfeitamente estendido e reto como uma ripa.",
                "Contenha a descida respirando suavemente sem prender a respiração."
            ),
            commonErrors = listOf(
                "Desabar o quadril criando lordose profunda por cansaço de core lombar.",
                "Elevar o quadril de forma excessiva imitando cabana."
            ),
            postureTips = listOf(
                "Tensione ativamente as nádegas para engajar as fáscias posteriores pélvicas."
            ),
            smartSubstitutions = listOf(
                "Perdigueiro (Bird Dog)",
                "Abdominal Crunch"
            ),
            easierVariation = "Prancha com joelhos apoiados no solo macio",
            harderVariation = "Prancha dinâmica comandando com braços escaladores",
            safetyWarnings = listOf(
                "O verdadeiro clássico biomecânico que reconstrói a estabilidade postural e cessa dores nas costas."
            ),
            tags = listOf("casa", "abdominais", "peso corporal", "iniciante", "core", "lombar"),
            primaryFocus = "Core Estabilização",
            iconDescription = "Abdominais"
        ),
        Exercise(
            id = "abdominal_crunch",
            name = "Abdominal Crunch",
            category = "Abdominais",
            primaryMuscle = "Reto Abdominal (Superior)",
            secondaryMuscles = "Oblíquos",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Definição de Gomos do Abdômen",
            executionSteps = listOf(
                "Deitese de costas em colchonete mantendo as pernas flexionadas a 90 graus com solas dos pés apoiadas no chão.",
                "Apoie suavemente os dedos sobre os lados das suas têmporas.",
                "Tire os ombros levemente do solo em cerca de 30 graus, contraindo com força o abdômen.",
                "Retorne controlando e mantendo a cabeça sem tocar o chão."
            ),
            commonErrors = listOf(
                "Puxar agressivamente com as mãos sua nuca colocando estresse mecânico sobre a coluna cervical.",
                "Subir o quadril inteiro saindo do solo de forma inadequada."
            ),
            postureTips = listOf(
                "Apenas retire as escápulas do chão espirando fôlego no ponto máximo de contração."
            ),
            smartSubstitutions = listOf(
                "Prancha Isométrica",
                "Perdigueiro (Bird Dog)"
            ),
            easierVariation = "Abdominal Crunch com apoios fáceis de pés em banco",
            harderVariation = "Abdominal infra pendurado na paralela",
            safetyWarnings = listOf(
                "Excelente se feito sem tirones. O queixo não deve encostar no esterno durante o movimento."
            ),
            tags = listOf("casa", "abdominais", "peso corporal", "iniciante"),
            primaryFocus = "Abdominais Isolamento",
            iconDescription = "Abdominais"
        ),
        Exercise(
            id = "bird_dog",
            name = "Perdigueiro (Bird Dog)",
            category = "Abdominais",
            primaryMuscle = "Multífidos / Transverso",
            secondaryMuscles = "Glúteo Máximo, Eretores da Espinha, Core total",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Estabilização Postural e Reabilitação de Hérnias",
            executionSteps = listOf(
                "Inicie em posição de quatro apoios no colchonete macio.",
                "Com controle fino, estenda de forma síncrona o braço direito e a perna esquerda ao mesmo tempo.",
                "Garanta estabilidade estática sustentando no ponto máximo por cerca de 3 segundos.",
                "Inverta alternando alternadamente as diagonais de controle."
            ),
            commonErrors = listOf(
                "Torcer o quadril excessivamente de banda tirando o centro de gravidade neutro.",
                "Arredondar a coluna lombar ao erguer membros."
            ),
            postureTips = listOf(
                "Mantenha a cabeça perfeitamente neutra olhando de forma calma para baixo."
            ),
            smartSubstitutions = listOf(
                "Prancha Isométrica",
                "Ponte de Glúteos no Solo"
            ),
            easierVariation = "Movimentar isoladamente apenas pernas e depois braços",
            harderVariation = "Perdigueiro executado sob instabilidade de prancha alta",
            safetyWarnings = listOf(
                "Principal componente preventivo listado pelo Dr. Stuart McGill para estabilizar a coluna sem tensões discogênicas."
            ),
            tags = listOf("casa", "abdominais", "peso corporal", "iniciante", "lombar", "reabilitacao"),
            primaryFocus = "Fortalecimento & Alívio Lombar",
            iconDescription = "Abdominais"
        ),
        Exercise(
            id = "calf_raises",
            name = "Gêmeos em Pé (Panturrilha)",
            category = "Pernas",
            primaryMuscle = "Gastrocnêmio",
            secondaryMuscles = "Sóleos, Tibial",
            equipmentNeeded = "Nenhum ou Halteres",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Força do Tornozelo e Definição de Canela",
            executionSteps = listOf(
                "Fique de pé em cima de um degrau ou no solo.",
                "Empurre com força a porção frontal de seus pés flexionando panturrilhas e subindo o calcanhar.",
                "Suba o máximo no topo sustentando por 1 segundo.",
                "Desça freando lentamente até alongar verticalmente a canela."
            ),
            commonErrors = listOf(
                "Fazer sob pulos rápidos, descarregando impacto nos tendões de aquiles sem ativação neuromuscular.",
                "Dobrar os joelhos para embalar."
            ),
            postureTips = listOf(
                "Gostaríamos de frear a descida por no mínimo 2 a 3 segundos para contração precisa."
            ),
            smartSubstitutions = listOf(
                "Extensão de Pé em leg press"
            ),
            easierVariation = "Gêmeos em pé no solo plano sem cargas",
            harderVariation = "Elevação de Panturrilha unilateral por perna com halter",
            safetyWarnings = listOf(
                "Ajuda a melhorar o retorno venoso circulatório sanguíneo preventivamente."
            ),
            tags = listOf("casa", "pernas", "peso corporal", "iniciante"),
            primaryFocus = "Resistência de Canela",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "cat_cow",
            name = "Alongamento Gato-Camelo",
            category = "Mobilidade",
            primaryMuscle = "Alongamento de Eretores de Coluna",
            secondaryMuscles = "Core, Coluna Torácica, Pescoço",
            equipmentNeeded = "Nenhum (Peso Corporal)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Mobilidade de Vértebras e Diminuição de Rigidez",
            executionSteps = listOf(
                "Fique em quatro apoios, mãos bem plantadas no solo debaixo dos ombros e joelhos debaixo dos quadris.",
                "Inale expandindo as costas para cima como um gato bravo, olhando em direção ao abdômen.",
                "Exale devagar arqueando a coluna lombar para baixo, estufando o peito e levantando o queixo.",
                "Flua ritmadamente entre as duas posições de forma controlada por 10 repetições."
            ),
            commonErrors = listOf(
                "Forçar as articulações de coluna rápido demais.",
                "Não esticar de forma inteira as vértebras."
            ),
            postureTips = listOf(
                "Sincronize perfeitamente a sua respiração para liberação do diafragma."
            ),
            smartSubstitutions = listOf(
                "Alongamento de Peito na Parede",
                "Perdigueiro (Bird Dog)"
            ),
            easierVariation = "Mobilização de bacia sentado",
            harderVariation = "Mobilidade lombar sob isometria estática prolongada",
            safetyWarnings = listOf(
                "Fabuloso aquecimento articular antes de agachamentos pesados ou treinos de costas."
            ),
            tags = listOf("casa", "mobilidade", "peso corporal", "iniciante", "lombar"),
            primaryFocus = "Mobilidade & Conforto",
            iconDescription = "Mobilidade"
        ),
        Exercise(
            id = "alongamento_peitoral",
            name = "Alongamento de Peito na Parede",
            category = "Mobilidade",
            primaryMuscle = "Alongamento Peitoral Maior/Menor",
            secondaryMuscles = "Deltoide Anterior, Tendão do bíceps",
            equipmentNeeded = "Nenhum (Parede)",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Alívio de Ombros Caídos e Postura de Escritório",
            executionSteps = listOf(
                "Apoie seu antebraço em quina de parede vertical ou batente de porta.",
                "Mantenha o cotovelo dobrado alinhado na altura de seus olhos ou peito.",
                "Gire o quadril e o peitoral de forma sutil para o lado contrário até sentir o peito alongar.",
                "Permaneça alongando respirando fundo por no mínimo 30 segundos e inverta."
            ),
            commonErrors = listOf(
                "Empurrar de forma saltada machucando os tendões frontais.",
                "Dar trancos bruscos."
            ),
            postureTips = listOf(
                "Estenda alongando com carinho e conforto, nunca busque sofrimento mecânico agudo."
            ),
            smartSubstitutions = listOf(
                "Alongamento dorsal no banco"
            ),
            easierVariation = "Giro de peito com menor ângulo lateral",
            harderVariation = "Alongamento de peitoral com os dois braços na porta",
            safetyWarnings = listOf(
                "Abre o gradil costal re-alinhando sua postura e cessando pinçamentos peitorais."
            ),
            tags = listOf("casa", "mobilidade", "peso corporal", "iniciante", "ombro"),
            primaryFocus = "Peitoral Flexibilidade",
            iconDescription = "Mobilidade"
        ),
        Exercise(
            id = "rotação_manguito",
            name = "Rotação Externa de Ombros",
            category = "Ombros",
            primaryMuscle = "Para manguito rotador (Infraespinhal, Redondo Menor)",
            secondaryMuscles = "Estabilizadores de ombros",
            equipmentNeeded = "Polia, Elástico ou Halter",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Prevenção de Lesões de Supino",
            executionSteps = listOf(
                "Segure um pequeno elástico fixador ou cabo em polia lateral ao corpo.",
                "Mantenha o braço dobrado a 90 graus exatos prensado contra costelas.",
                "Afastar de banda o antebraço rotacionando o ombro para fora de forma isolada.",
                "Retorne segurando a carga suave sem afundar os ombros."
            ),
            commonErrors = listOf(
                "Afastar o cotovelo abrindo o braço lateralmente gerando esforço de deltoide.",
                "Exagerar na carga gerando estalos mecânicos de dor anterior."
            ),
            postureTips = listOf(
                "Pressione uma pequena toalha dobrada sob a axila para garantir isolamento rotador mecânico."
            ),
            smartSubstitutions = listOf(
                "Face Pull na Polia"
            ),
            easierVariation = "Rotação de manguito deitado de lado com halter levíssimo do lado de fora",
            harderVariation = "Rotação externa sob elevação lateral em isometria no ângulo de ombro",
            safetyWarnings = listOf(
                "Ideal executora protetora de ombro antes de qualquer exercício pesado de supinar."
            ),
            tags = listOf("casa", "ombros", "polia", "iniciante", "reabilitacao"),
            primaryFocus = "Estabilização Ombro",
            iconDescription = "Ombros"
        ),
        Exercise(
            id = "copenhagen_plank",
            name = "Prancha de Copenhagen",
            category = "Abdominais",
            primaryMuscle = "Adutores do Quadril",
            secondaryMuscles = "Oblíquos, Reto Abdominal, Glúteo Médio",
            equipmentNeeded = "Banco",
            difficultyLevel = "Avançado",
            recommendedObjective = "Estabilização Pélvica & Prevenção de Pubalgia",
            executionSteps = listOf(
                "Deite-se de lado e apoie a perna de cima em um banco rígido na altura do joelho ou tornozelo.",
                "Apoie o antebraço de baixo no chão, alinhado com o ombro.",
                "Eleve o quadril do solo formando uma linha reta, mantendo a perna de baixo suspensa no ar.",
                "Sustente a posição contraindo fortemente os adutores e o core."
            ),
            commonErrors = listOf(
                "Deixar o quadril cair ou rotacionar para a frente.",
                "Sentir dor aguda na parte interna do joelho por falta de alinhamento."
            ),
            postureTips = listOf(
                "Comece apoiando o banco perto do joelho para reduzir o braço de alavanca se for muito difícil."
            ),
            smartSubstitutions = listOf(
                "Cadeira Adutora",
                "Elevação Lateral de Perna deitado"
            ),
            easierVariation = "Prancha de Copenhagen Curta (apoio no joelho)",
            harderVariation = "Prancha de Copenhagen Longa (apoio no tornozelo) com movimentos dinâmicos",
            safetyWarnings = listOf(
                "Evite se tiver lesões agudas de adutor ou estiramentos na virilha recentes."
            ),
            tags = listOf("academia", "casa", "abdominais", "pernas", "avancado", "core", "reabilitacao"),
            primaryFocus = "Saúde do Quadril & Estabilidade",
            iconDescription = "Abdominais"
        ),
        Exercise(
            id = "tibialis_raise",
            name = "Elevação Tibial na Parede",
            category = "Pernas",
            primaryMuscle = "Tibial Anterior",
            secondaryMuscles = "Tendões do Tornozelo, Sóleo",
            equipmentNeeded = "Parede",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Proteção contra Canelite & Desaceleração de Impacto",
            executionSteps = listOf(
                "Encoste o bumbum e as costas na parede com os pés posicionados cerca de 30-50 cm à frente.",
                "Mantendo as pernas esticadas, eleve as pontas dos pés do chão o máximo que puder, apoiando-se apenas nos calcanhares.",
                "Segure a contração máxima no topo por 1 segundo.",
                "Desça lentamente até apoiar a sola dos pés no chão e repita."
            ),
            commonErrors = listOf(
                "Flexionar os joelhos durante a subida.",
                "Não alcançar a amplitude total de movimento."
            ),
            postureTips = listOf(
                "Quanto mais longe os pés estiverem da parede, mais difícil será o exercício."
            ),
            smartSubstitutions = listOf(
                "Caminhada nos calcanhares"
            ),
            easierVariation = "Elevação Tibial sentado",
            harderVariation = "Elevação Tibial na parede com peso nos pés (Tibia Barra)",
            safetyWarnings = listOf(
                "Excelente para corredores que sofrem de dor na canela (canelite) e para melhorar a desaceleração do joelho."
            ),
            tags = listOf("casa", "pernas", "peso corporal", "iniciante", "canela", "reabilitacao"),
            primaryFocus = "Desaceleração & Canela Forte",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "pallof_press",
            name = "Prensa Pallof (Anti-Rotação)",
            category = "Abdominais",
            primaryMuscle = "Oblíquos do Abdômen",
            secondaryMuscles = "Transverso, Reto Abdominal, Glúteos, Estabilizadores escapulares",
            equipmentNeeded = "Polia ou Elástico",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Estabilização de Core e Alívio Lombar",
            executionSteps = listOf(
                "Posicione-se de lado para a polia ou fixação do elástico na altura do peito.",
                "Segure a manopla com as duas mãos rente ao peito e dê alguns passos para o lado para criar tensão.",
                "Adote uma base atlética de pé: joelhos microflexionados e core ativo.",
                "Estenda os braços à frente em linha reta, resistindo ativamente à força lateral que tenta girar seu tronco.",
                "Segure por 2 segundos e retorne as mãos ao peito de forma controlada."
            ),
            commonErrors = listOf(
                "Permitir que o tronco gire em direção à polia.",
                "Subir os ombros em direção às orelhas."
            ),
            postureTips = listOf(
                "Mantenha os quadris e ombros apontados para a frente o tempo todo; o movimento deve ocorrer apenas nos braços."
            ),
            smartSubstitutions = listOf(
                "Prancha Lateral",
                "Abdominal Oblíquo Woodchopper"
            ),
            easierVariation = "Pallof Press de Joelhos",
            harderVariation = "Pallof Press com base unilateral (Staggered Stance)",
            safetyWarnings = listOf(
                "Maravilhoso exercício de estabilização sem flexão da coluna, muito recomendado para quem tem hérnia de disco."
            ),
            tags = listOf("academia", "casa", "abdominais", "polia", "iniciante", "core", "lombar"),
            primaryFocus = "Anti-Rotação & Proteção Lombar",
            iconDescription = "Abdominais"
        ),
        Exercise(
            id = "spanish_squat",
            name = "Agachamento Espanhol (Isometria)",
            category = "Pernas",
            primaryMuscle = "Quadríceps",
            secondaryMuscles = "Glúteo Máximo, Isquiotibiais",
            equipmentNeeded = "Faixa Elástica Grossa (Superband)",
            difficultyLevel = "Intermediário",
            recommendedObjective = "Reabilitação de Tendão Patelar & Isometria Pesada",
            executionSteps = listOf(
                "Prenda uma faixa elástica grossa e resistente em um poste estável na altura dos joelhos.",
                "Coloque as alças elásticas atrás dos joelhos (região poplítea) e dê passos para trás para criar alta tensão.",
                "Com o corpo verticalizado, agache sentando-se para trás enquanto o elástico puxa seus joelhos para a frente.",
                "Desça até cerca de 75-90 graus e sustente a posição isométrica pelo tempo prescrito."
            ),
            commonErrors = listOf(
                "Deixar o tronco inclinar demais à frente.",
                "Permitir que os joelhos ultrapassem demais os dedos dos pés (perde a alavanca do elástico)."
            ),
            postureTips = listOf(
                "Mantenha o peso nos calcanhares e force os joelhos ativamente contra a faixa elástica."
            ),
            smartSubstitutions = listOf(
                "Agachamento na parede (Wall Sit)",
                "Cadeira Extensora isométrica"
            ),
            easierVariation = "Agachamento Espanhol com menor amplitude",
            harderVariation = "Agachamento Espanhol dinâmico com halteres",
            safetyWarnings = listOf(
                "Gold standard clínico do esporte para aliviar dores de tendinite patelar crônica."
            ),
            tags = listOf("casa", "academia", "pernas", "reabilitacao", "intermediario", "joelho"),
            primaryFocus = "Saúde Patelar & Força Isométrica",
            iconDescription = "Pernas"
        ),
        Exercise(
            id = "kelso_shrug",
            name = "Encolhimento Kelso (Escapular)",
            category = "Costas",
            primaryMuscle = "Trapézio Médio e Romboides",
            secondaryMuscles = "Deltoide Posterior, Estabilizadores do Ombro",
            equipmentNeeded = "Halteres e Banco Inclinado",
            difficultyLevel = "Iniciante",
            recommendedObjective = "Postura Escapular & Saúde dos Ombros",
            executionSteps = listOf(
                "Deite-se de bruços em um banco inclinado a cerca de 30-45 graus, segurando um halter em cada mão com os braços suspensos.",
                "Mantendo os cotovelos totalmente estendidos durante todo o movimento, retraia as escápulas (aproxime-as uma da outra).",
                "Segure a contração máxima atrás por 2 segundos e retorne abrindo/protraindo as escápulas."
            ),
            commonErrors = listOf(
                "Flexionar os braços como se fosse uma remada convencional (ativa bíceps em vez das escápulas).",
                "Usar o trapézio superior (dar de ombros em direção às orelhas)."
            ),
            postureTips = listOf(
                "Foque no movimento puro de abrir e fechar as asas das costas sem mexer os cotovelos."
            ),
            smartSubstitutions = listOf(
                "Remada Escapular na Barra Fixa",
                "Crucifixo Invertido"
            ),
            easierVariation = "Encolhimento Kelso sem carga (apenas peso corporal)",
            harderVariation = "Encolhimento Kelso com pausa isométrica de 5s no topo",
            safetyWarnings = listOf(
                "Perfeito para alinhar escápulas aladas e combater o encurtamento postural de ombros."
            ),
            tags = listOf("academia", "casa", "costas", "halteres", "iniciante", "postura"),
            primaryFocus = "Estabilidade Escapular & Postura",
            iconDescription = "Costas"
        )
    )
}
