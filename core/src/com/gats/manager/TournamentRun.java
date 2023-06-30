package com.gats.manager;

import java.util.*;

public class TournamentRun extends Run {


    private class BracketNode {

        private final Object handlerLock = new Object();
        private final Object schedulingLock = new Object();

        private final List<CompletionHandler<BracketNode>> handlers = new ArrayList<>();


        Game game;

        private int p1 = -1;
        private int p2 = -1;

        boolean winner = false;
        private final GameConfig config;

        private boolean completed = false;

        private BracketNode(GameConfig config) {
            this.config = config;
        }

        public void makeLeaf(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
            startGame();
        }

        BracketNode left;
        BracketNode right;

        public void setLeft(BracketNode left) {
            this.left = left;
            left.addCompletionListener(this::onLeftCompletion);
        }

        public void setLeft(int p1) {
            synchronized (schedulingLock) {
                this.p1 = p1;
                if (p2 > -1) startGame();
            }
        }
        protected void onLeftCompletion(BracketNode left) {
            setLeft(left.getWinner());
        }

        public void setRight(BracketNode right) {
            this.right = right;
            right.addCompletionListener(this::onRightCompletion);
        }

        public void setRight(int p2) {
            synchronized (schedulingLock) {
            this.p2 = p2;
                if (p1 > -1) startGame();
            }
        }

        protected void onRightCompletion(BracketNode right) {
            setRight(right.getWinner());
        }

        public int getWinner(){
            return winner ? p2 : p1;
        }

        public int getLooser(){
            return winner ? p1 : p2;
        }

        private void startGame() {
            config.players = new ArrayList<>();
            config.players.add(players.get(p1));
            config.players.add(players.get(p2));
            game = new Game(config);
            game.addCompletionListener(this::onGameComplete);
            manager.schedule(game);
        }

        void onGameComplete(Executable exec){
            Game game = (Game) exec;
            synchronized (handlerLock) {
                completed = true;
                winner = (game.getScores()[0] < game.getScores()[1]);
                for (CompletionHandler<BracketNode> handler : handlers) {
                    handler.onComplete(this);
                }
            }
        }

        void addCompletionListener(CompletionHandler<BracketNode> handler) {
            synchronized (handlerLock) {
                handlers.add(handler);
                if (completed) handler.onComplete(this);
            }
        }
    }

    private class LooserBracket extends BracketNode{

        private LooserBracket(GameConfig config) {
            super(config);
        }

        @Override
        protected void onLeftCompletion(BracketNode left) {
            setLeft(left.winner ? left.p1 : left.p2);
        }

        @Override
        protected void onRightCompletion(BracketNode right) {
            setRight(right.winner ? right.p1 : right.p2);
        }
    }


    private final ArrayList<Class<? extends Player>> players;

    int completedGames = 0;

    private BracketNode finalGame;
    private BracketNode looserBracket;

    private final float[] scores;

    protected TournamentRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        if (runConfig.teamCount != 2)
            System.err.printf("Warning: Only 1v1 is supported in bracket tournaments. Ignoring config.teamCount = %d%n", runConfig.teamCount);
        players = runConfig.players;

        int playerCount = players.size();
        scores = new float[playerCount];
        if (playerCount < 2) {
            System.err.println("A Tournament requires at least 2 players");
            return;
        }

        if ((playerCount & playerCount - 1) != 0)
            System.err.printf("Warning: Number of players(=%d) is no power of two. Some players will skip the first tournament round %n", playerCount);

        finalGame = new BracketNode(new GameConfig(runConfig));
        finalGame.addCompletionListener(this::onRootCompletion);
        Queue<BracketNode> upperLeafGames = new ArrayDeque<>();
        Queue<BracketNode> lowerLeafGames = new ArrayDeque<>();
        BracketNode leftFilled = null;
        upperLeafGames.add(finalGame);
        int capacity = 2;
        while (capacity < playerCount) {
            BracketNode newNode = new BracketNode(new GameConfig(runConfig));
            if (leftFilled == null) {
                if (upperLeafGames.isEmpty()) {
                    upperLeafGames = lowerLeafGames;
                    lowerLeafGames = new ArrayDeque<>();
                }
                BracketNode cur = upperLeafGames.poll();
                assert cur != null;
                cur.setLeft(newNode);
                leftFilled = cur;
            } else {
                BracketNode cur = leftFilled;
                leftFilled = null;
                cur.setRight(newNode);
            }
            lowerLeafGames.add(newNode);
            capacity++;
        }
        int pIndex = 0;
        while (!lowerLeafGames.isEmpty()) {
            BracketNode curNode = lowerLeafGames.poll();
            curNode.makeLeaf(pIndex++, pIndex++);
        }

        if (leftFilled != null) leftFilled.setRight(pIndex++);

        while (!upperLeafGames.isEmpty()) {
            BracketNode curNode = upperLeafGames.poll();
            curNode.makeLeaf(pIndex++, pIndex++);
        }
        if (pIndex>3){
            looserBracket = new LooserBracket(new GameConfig(runConfig));
            looserBracket.setLeft(finalGame.left);
            looserBracket.setRight(finalGame.right);
            looserBracket.addCompletionListener(this::onRootCompletion);
        }
        assert pIndex == playerCount;

    }

    public synchronized void onRootCompletion(BracketNode node) {
        if (looserBracket == null){
            assert node == finalGame;
            complete();
            return;
        }
        completedGames++;
        if (completedGames == 2) complete();
    }

    @Override
    protected void complete() {

        int winner = finalGame.getWinner();
                int second = finalGame.getLooser();
        if (looserBracket != null){
            scores[looserBracket.getWinner()] = 3f;
            scores[looserBracket.getLooser()] = 4f;
            setLooserScore(finalGame.left.left, 5f);
            setLooserScore(finalGame.left.right, 5f);
            setLooserScore(finalGame.right.left, 5f);
            setLooserScore(finalGame.right.right, 5f);
        }else {
            if(finalGame.left!= null) scores[finalGame.getLooser()] = 3f;
        }
        scores[winner] = 1f;
        scores[second] = 2f;

        super.complete();
    }

    private void setLooserScore(BracketNode node, float score){
        if (node == null) return;
        scores[node.getLooser()] = score;
        setLooserScore(node.left, score +1);
        setLooserScore(node.right, score +1);

    }

    @Override
    public float[] getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "TournamentRun{" +
                "super=" + super.toString() +
                ", players=" + players +
                ", completedGames=" + completedGames +
                ", finalGame=" + finalGame +
                ", looserBracket=" + looserBracket +
                ", scores=" + Arrays.toString(scores) +
                '}';
    }
}
