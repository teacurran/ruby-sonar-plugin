# Test comment 1
# Test comment 2
class Greeter
  def initialize(name = nil)
    @name = name
  end

  def greet
    greeting = "Hello #{@name ||= 'world'}!"
    greeting
  end

  def repeat(n)
    greeting = (greet + '/n')*n
    greeting
    # Commented out code
    # puts greeting
  end
end
