----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 01/03/2024 09:02:53 AM
-- Design Name: 
-- Module Name: Main - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
-- Description: 
-- 
-- Dependencies: 
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;


entity Main is
 Port (clk: in STD_LOGIC;
       rst: in STD_LOGIC;
       scl: inout STD_LOGIC;
       sda: inout STD_LOGIC;
       err: buffer STD_LOGIC;
       an: out std_logic_vector(7 downto 0);
       seg: out std_logic_vector(7 downto 0));
end Main;

architecture Behavioral of Main is
signal temperature: std_logic_vector(12 downto 0);
signal temp_out : std_logic_vector(15 downto 0);
signal data: std_logic_vector(31 downto 0);
signal not_rst: STD_LOGIC;


 -- UART signals
  signal uart_clk    : std_logic;
  signal uart_rst    : std_logic;
  signal uart_start  : std_logic := '1';
  signal uart_txdata : std_logic_vector(7 downto 0);
  signal uart_tx     : std_logic;
  signal uart_txrdy  : std_logic;


begin

temp_out <= "000" & temperature;
data <= x"0000" & temp_out;
not_rst <= not rst;

temp: entity WORK.Temperatura port map ( clk, not_rst, scl, sda, err,temperature);
                                 
                                  
display: entity WORK.display_7seg port map (clk, rst,data,an, seg);
                                         
                                
debouncer: entity WORK.debouncer port map (clk ,rst, not_rst);

start_but: entity Work.debouncer port map (clk,rst,uart_start); 

 uart_clk    <= clk;   -- Connect the main clock to UART
  uart_rst    <= rst;   -- Connect the main reset to UART
  uart_txdata <= std_logic_vector(resize(unsigned(data(31 downto 24)), 8));  -- Convert 32-bit to 8-bit  -- Adjust the bits based on your needs

  uart: entity WORK.uart_tx
    generic map (
      n => 115200  -- Baud rate folosit la laborator
    )
    port map (
      Clk     => clk,
      Rst     => rst,
      Start   => uart_start,
      TxData  => uart_txdata,
      Tx      => uart_tx,
      TxRdy   => uart_txrdy
    );
    

                                           
end Behavioral;
