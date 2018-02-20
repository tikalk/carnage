import React, { Component } from 'react'
import './question.component.css'

class Question extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedAnswer: this.props.question.answers[0]
        }
    }

    answerSubmitted = false;
    timeoutReached = false;
    message = '';

    static TIMEOUT = 5*1000;

    componentDidMount() {
        setTimeout(() => {
            this.timeoutReached = true;
        },Question.TIMEOUT)
    }

    render() {
        return (
            <div className="App">
                <h1>{this.props.question.question}</h1>
                <form>
                    <div class="answers">
                        {
                            this.props.question.answers.map((item, index) => (
                                <div>
                                    <input type="radio" value={item} 
                                        checked={this.state.selectedAnswer === item} 
                                        onChange={(event) => this.handleOptionChange(event)}/>
                                    <h3 className='answer' key={index}>{item}</h3>
                                </div>
                            ))
                        }
                    </div>
                </form>
                
                
                <button className="submit-button" onClick={(event) => this.submitResponse(event)}>Submit</button>
                <div className="message">
                { 
                    this.answerSubmitted && 
                    this.message
                }
                </div>
                <div className="timer">
                    <div class="wrapper">
                        <div class="pie spinner"></div>
                        <div class="pie filler"></div>
                        <div class="mask"></div>
                    </div>
                </div>
            </div>
        );
    }

    submitResponse(event) {

        if (this.answerSubmitted && !this.timeoutReached) {
            this.message = 'Answer Already Submitted';
        } else if (this.timeoutReached) {
            this.message = 'Sorry - Time is up';
        } else {
            this.message = 'Thank You!';
        }

        this.answerSubmitted = true;
        this.forceUpdate()
    }

    handleOptionChange(changeEvent) {
        this.setState({
            selectedAnswer: changeEvent.target.value
        });
        this.forceUpdate()
    }
}

export default Question;