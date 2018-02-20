import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        }
    }
    render() {
        return (
            <div>
                <MuiThemeProvider>
                    <div>
                        <AppBar
                            title="Login"
                        />
                        <TextField
                            hintText="Enter your Username"
                            floatingLabelText="Username"
                            onChange={(event, newValue) => this.setState({ username: newValue })}
                        />
                        <br />
                        <TextField
                            type="password"
                            hintText="Enter your Password"
                            floatingLabelText="Password"
                            onChange={(event, newValue) => this.setState({ password: newValue })}
                        />
                        <br />
                        <RaisedButton label="Submit" primary={true} style={style} onClick={(event) => this.handleClick(event)} />
                    </div>
                </MuiThemeProvider>
            </div>
        );
    }

    componentWillMount () {
        const script = document.createElement("script");

        script.src = "https://sdk.amazonaws.com/js/aws-sdk-2.3.7.min.js";
        script.async = true;
        script.onload = () => {
            window.AWS.config.region = 'us-east-1';
            window.AWS.config.credentials = new window.AWS.CognitoIdentityCredentials({
                IdentityPoolId: 'us-east-1:1b35133c-15bc-4807-a1fa-367218df73f4'
            });
            this.lambda = new window.AWS.Lambda();
        }
        document.body.appendChild(script);
    }

    handleClick(event) {
        var apiBaseUrl = "http://localhost:4000/api/";
        var self = this;

        var input = {
            email: this.state.username,
            password: this.state.password
        }
        this.lambda.invoke({
            FunctionName: 'LambdAuthLogin',
            Payload: JSON.stringify(input)
        }, function (err, data) {

            if (err) console.log(err, err.stack);
            else {
                var output = JSON.parse(data.Payload);
                if (!output.login) {
                    alert('Error on login');
                } else {
                    self.props.appContext.props.onLogin();
                }
            }
        });
    }
}
const style = {
    margin: 15,
};



export default Login;