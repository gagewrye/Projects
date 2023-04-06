/*
This game is intended to be run on a Red Thing Plus board using the Risc-V Architecture. It uses direct memory mapping to
set the inputs and outputs.

Pin 21 is the output for led 1 and pin 1 should read the voltage on the led.
Pin 20 is the output for led 1 and pin 2 reads the voltage.

The leds should be connected to separate switches that send power to the LED when pressed to allow user input.

The game is a simple see and repeat Simon-style game where the user presses the led switch in the order that they flash.
If they get it correct, then the sequence is increased by 1 and a new randomized sequence is generated.
*/

#include <stdint.h>

// Define GPIO register addresses
#define GPIO_BASE  0x10012000
#define INPUT_ENABLE  0x10012004
#define OUTPUT_ENABLE 0x10012008
#define GPIO_OUTPUT_VAL_ADDR 0x1001200C
#define GPIO_PULLUP_EN_ADDR 0x10012010

// Define LED and switch pin numbers
#define LED1 21
#define LED2 20 
#define SWITCH1 1 
#define SWITCH2 2

// Function prototypes
void init_gpio();
void generate_sequence(uint8_t* sequence, uint8_t length);
uint8_t read_sequence(uint8_t* sequence, uint8_t length);
void flash_leds(uint8_t* sequence, uint8_t length);
void delay(unsigned int milliseconds);


int main()
{
    init_gpio();
    uint8_t sequence_length = 2;
    uint8_t* sequence[12];
 
 // Game loop
 while (1)
    {
        generate_sequence(sequence, sequence_length);

        // wait between rounds
        delay(1500);

        flash_leds(sequence, sequence_length);

        // Wait for the player to repeat the sequence
        int correct = read_sequence(sequence, sequence_length);

        if (correct)
        {
            sequence_length++;
            if (sequence_length > 12){sequence_length = 12;} // TODO: ADD WIN CONDITION
        }
        // Restart if player makes a mistake
        else{sequence_length = 2;}
    }

    return 0;
}

void init_gpio()
{
    // Set GPIO pins as input or output
    *(volatile uint32_t*)INPUT_ENABLE |= (1 << SWITCH1) | (1 << SWITCH2);
    *(volatile uint32_t*)OUTPUT_ENABLE |= (1 << LED1) | (1 << LED2);

    // Enable pull-up resistors for Inputs
    *(volatile uint32_t*)GPIO_PULLUP_EN_ADDR |= (1 << SWITCH1) | (1 << SWITCH2);
}


void generate_sequence(uint8_t* sequence, uint8_t length)
{
     // Generate a random sequence of 1s and 2s
    for (int i = 0; i < length; i++)
    {
        sequence[i] = rand() % 2 + 1; // generate a random number 1 or 2
    }
}

void flash_leds(uint8_t* sequence, uint8_t length)
{
    volatile uint32_t* const GPIO_BASE_ADDR = (uint32_t*)OUTPUT_ENABLE;
    volatile uint32_t* const GPIO_BASE_ADDR_EN = (uint32_t*)GPIO_OUTPUT_VAL_ADDR;

    for (int i = 0; i < length; i++)
    {
        if (sequence[i] == 1)
        {
            *(GPIO_BASE_ADDR_EN) &= ~(1 << LED2); // set pin 20 low
            *(GPIO_BASE_ADDR_EN) |= (1 << LED1);  // set pin 21 high
        }
        else
        {
            *(GPIO_BASE_ADDR_EN) |= (1 << LED2); // set pin 21 high
            *(GPIO_BASE_ADDR_EN) &= ~(1 << LED1);  // set pin 20 low
        }
        delay(500);
        *(GPIO_BASE_ADDR_EN) &= ~(1 << LED2); // set both pins low
        *(GPIO_BASE_ADDR_EN) &= ~(1 << LED1);
        delay(500); 
    }
}

uint8_t read_sequence(uint8_t* sequence, uint8_t length)
{
    uint8_t player_input[length];
    for (uint8_t i = 0; i < length; i++)
    {
        // Wait for a button press
        while (!(*(volatile uint32_t*)GPIO_BASE & (1 << SWITCH1)) && !(*(volatile uint32_t*)GPIO_BASE & (1 << SWITCH2))) {}

        // Determine which button was pressed
        player_input[i] = (*(volatile uint32_t*)GPIO_BASE & (1 << SWITCH1)) ? 1 : 2;
        
        // Wait for button release
        while ((*(volatile uint32_t*)GPIO_BASE & (1 << SWITCH1)) || (*(volatile uint32_t*)GPIO_BASE & (1 << SWITCH2))) {}

        // Check if the player input matches the sequence
        if (player_input[i] != sequence[i])
        {
            return 0;
        }
    }

    return 1;
}

void delay(unsigned int milliseconds)
{
    for (unsigned int i = 0; i < milliseconds; i++)
    {
        for (unsigned int j = 0; j < 1000; j++){}
    }
}