import React, { Component } from 'react'
import './question.component.css'
import axios from 'axios'

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
                    <div className="answers">
                        {
                            this.props.question.answers.map((item, index) => (
                                <div key={index}>
                                    <input type="radio" value={item} 
                                        checked={this.state.selectedAnswer === item} 
                                        onChange={(event) => this.handleOptionChange(event)}/>
                                    <h3 className='answer'>{item}</h3>
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
                    <div className="wrapper">
                        <div className="pie spinner"></div>
                        <div className="pie filler"></div>
                        <div className="mask"></div>
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

            axios.post('/user', {
                firstName: 'Fred',
                lastName: 'Flintstone'
            })
            .then((function(that){
                return () => {
                    that.message = 'You answer was submitted'
                    that.props.onSubmit();
                    that.forceUpdate();
                }
            })(this))
            .catch((function(that){
                return (error) => {
                    that.message = error.message;
                    that.props.onSubmit();
                    that.forceUpdate();
                }
            })(this));
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