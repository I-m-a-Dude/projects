----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 11/22/2023 08:13:46 AM
-- Design Name: 
-- Module Name: uart_tx - Behavioral
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

-- UART de la Laboratorul 8 de SSC

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED;
use IEEE.NUMERIC_STD.ALL;


entity uart_tx is generic( n : integer := 115200);
    Port ( Clk : in std_logic;
    	   Rst : in std_logic;
    	   Start : in std_logic;
    	   TxData : in std_logic_vector(7 downto 0);
    	   Tx : out std_logic;
    	   TxRdy : out std_logic );
end uart_tx;

architecture Behavioral of uart_tx is
type stare is (ready, load, send, waitbit, shift);
attribute keep : STRING;
signal St : stare := ready;
signal LdData, ShData, TxEn : std_logic := '0';
signal CntRate, CntBit : integer := 0;
constant clk_frq : integer := 100000000;
constant T_BIT : integer := 868;
signal TSR : std_logic_vector(9 downto 0) := (others=>'0');
attribute keep of St, CntRate, CntBit, TSR : signal is "TRUE";
begin

-- Automatul de stare pentru unitatea de control
 proc_control: process (Clk)
 begin
 if RISING_EDGE (Clk) then
 	if (Rst = '1') then
 		St <= ready;
 	else
 		case St is
 				when ready =>
 						CntRate <= 0;
						CntBit <= 0;
						if (Start = '1') then
 						St <= load;
 						end if;
 				when load =>
 						St <= send;
 				when send =>
 						St <= waitbit;
 						CntBit <= CntBit + 1;
 				when waitbit =>
 						CntRate <= CntRate + 1;
				if (CntRate = T_BIT - 3) then
 						CntRate <= 0;
						St <= shift;
 				end if;
 				when shift =>
 						if (CntBit = 10) then
 							St <= ready;
 						else
 						St <= send;
 						end if;
 				when others =>
 						St <= ready;
 		end case;
 	end if;
 end if;
 
 end process proc_control;
-- Setarea semnalelor de comanda

 LdData <= '1' when St = load else '0';
 ShData <= '1' when St = shift else '0';
 TxEn <= '0' when St = ready or St = load else '1';

-- Setarea semnalelor de iesire
 Tx <= TSR(0) when TxEn = '1' else '1';
 TxRdy <= '1' when St = ready else '0';
 
 process (Clk, Rst)
 begin
 	if (Clk'event and Clk='1') then
 		if (Rst = '1') then
 			TSR <= (others => '0');
 		else
 			if (LdData = '1') then
 				TSR <= '1' & TxData & '0';
 			elsif (ShData = '1') then
 				TSR <= '0' & TSR(9 downto 1);
 			end if;
 		end if;
 	end if;
 end process;

end Behavioral;
