import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Cobra extends DesenhoMovel implements ActionListener {
	
	private final int LARGURA = 400;
    private final int ALTURA = 400;
    private final int TAM_OBJETO = 10;
    private final int TOTAL_PONTOS = LARGURA * ALTURA;
    private final int POS_MACA = 30;
    private final int VELOCIDADE = 70;
	private final int cobraX[] = new int[TOTAL_PONTOS];
    private final int cobraY[] = new int[TOTAL_PONTOS];
    
	private int tamCorpo;
    private int macaX;
    private int macaY;
    
    private boolean movendoDireita = true;
    private boolean movendoEsquerda = false;
    private boolean movendoBaixo = false;
    private boolean movendoCima = false;
    private boolean execultaJogo = true;
    
    private Timer tempo;
    private Image corpo;
    private Image maca;
    private Image cabeca;
    
    private static int pontuacao = 0;

	public static int getPontuacao() {
		return pontuacao;
	}

	public Cobra() 
    {        
        addKeyListener(new movimento());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(LARGURA, ALTURA));
        carregaImagens();
        iniciaJogo();
    }
    
    public void carregaImagens() 
    {
        ImageIcon co = new ImageIcon("corpo.png");
        corpo = co.getImage();

        ImageIcon ma = new ImageIcon("maca.png");
        maca = ma.getImage();

        ImageIcon ca = new ImageIcon("cabeca.png");
        cabeca = ca.getImage();
    }
    
    public void iniciaJogo() 
    {
        tamCorpo = 3;

        for (int i = 0; i < tamCorpo; i++) 
        {
        	cobraX[i] = 150 - i * 10;
        	cobraY[i] = 150;
        }

        posicionaMaca();

        tempo = new Timer(VELOCIDADE, this);
        tempo.start();
    }
    
    public void fimJogo(Graphics g) 
    {
    	String pontos = "Pontuação: " + getPontuacao();
        String msg = "Fim de Jogo";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (LARGURA - metr.stringWidth(msg)) / 2, (ALTURA / 2) - 50);
        g.drawString(pontos, (LARGURA - metr.stringWidth(pontos)) / 2, ALTURA / 2);
        
        //JFrame jogo = new Principal();
        //jogo.setVisible(true);
    }
    
    public void escreveObjetos(Graphics g) 
    {
        if (execultaJogo) 
        {
            g.drawImage(maca, macaX, macaY, this);

            for (int z = 0; z < tamCorpo; z++) 
            {
                if (z == 0) 
                {
                    g.drawImage(cabeca, cobraX[z], cobraY[z], this);
                } else 
                {
                    g.drawImage(corpo, cobraX[z], cobraY[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } 
        else 
        {
            fimJogo(g);
        }        
    }

    public void posicionaMaca()
    {
        int r = (int) (Math.random() * POS_MACA);
        macaX = ((r * TAM_OBJETO));

        r = (int) (Math.random() * POS_MACA);
        macaY = ((r * TAM_OBJETO));
    }
    
    public void checaMaca() 
    {
        if ((cobraX[0] == macaX) && (cobraY[0] == macaY)) 
        {
        	tamCorpo++;
        	pontuacao++;
        	posicionaMaca();
        }
    }

    public void mover() 
    {
        for (int i = tamCorpo; i > 0; i--) 
        {
        	cobraX[i] = cobraX[(i - 1)];
        	cobraY[i] = cobraY[(i - 1)];
        }

        if (movendoDireita) 
        {
        	cobraX[0] += TAM_OBJETO;
        }

        if (movendoEsquerda) 
        {
        	cobraX[0] -= TAM_OBJETO;
        }

        if (movendoBaixo) 
        {
        	cobraY[0] += TAM_OBJETO;
        }

        if (movendoCima) 
        {
        	cobraY[0] -= TAM_OBJETO;
        }
    }

    public void checaMovimento() 
    {
        for (int i = tamCorpo; i > 0; i--) 
        {
            if ((cobraX[0] == cobraX[i]) && (cobraY[0] == cobraY[i])) 
            {
            	execultaJogo = false;
            }
        }

        if (cobraY[0] >= ALTURA) 
        {
        	execultaJogo = false;
        }

        if (cobraY[0] < 0) 
        {
        	execultaJogo = false;
        }

        if (cobraX[0] >= LARGURA) 
        {
        	execultaJogo = false;
        }

        if (cobraX[0] < 0) 
        {
        	execultaJogo = false;
        }
        
        if(!execultaJogo) 
        {
            tempo.stop();
        }
    }

	public class movimento extends KeyAdapter 
	{
        @Override
        public void keyPressed(KeyEvent e) 
        {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_RIGHT) && (!movendoEsquerda)) 
            {
            	movendoDireita = true;
            	movendoCima = false;
                movendoBaixo = false;
            }
            
            if ((key == KeyEvent.VK_LEFT) && (!movendoDireita)) 
            {
            	movendoEsquerda = true;
            	movendoCima = false;
                movendoBaixo = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!movendoCima)) 
            {
            	movendoBaixo = true;
                movendoDireita = false;
                movendoEsquerda = false;
            }

            if ((key == KeyEvent.VK_UP) && (!movendoBaixo)) 
            {
            	movendoCima = true;
                movendoDireita = false;
                movendoEsquerda = false;
            }
        }
    }    

	//Método chamando para execultar o jogo na interface
	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (execultaJogo) 
        {
        	checaMaca();
            checaMovimento();
            mover();
        }
        repaint();
    }
	
	//Método para ficar escrevendo na tela as imagens da maçã e do corpo da cobra
	public void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        escreveObjetos(g);
    }

}
