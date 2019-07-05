import React, {Component} from 'react';
import axios from 'axios';
import './style.css'
import './developers.css'
import './about.css'
import './button.css'
import './input-box.css'

export class Test extends Component {

    constructor(props) {
        super(props);
        this.state = {
            default: true,
            developers: false,
            techSpecs: false,
            faq: false,
            login: false,
            download: false,
            about: false
        };
    }

    render() {
        return (
            <>
                <div className={"header"}>
                    <div className={"left"}>
                        <div className={"sideBySide"}>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: true,
                                techSpecs: false,
                                faq: false,
                                login: false,
                                download: false,
                                about: false
                            })}><i className="fas fa-code"/> Developers</p>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: false,
                                techSpecs: true,
                                faq: false,
                                login: false,
                                download: false,
                                about: false
                            })}><i className="fas fa-memory"/> Tech Specs</p>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: false,
                                techSpecs: false,
                                faq: true,
                                login: false,
                                download: false,
                                about: false
                            })}><i className="fas fa-question"/> FAQ</p>
                        </div>
                    </div>
                    <div className={"center"}>
                        <p onClick={() => this.setState({
                            default: true,
                            developers: false,
                            techSpecs: false,
                            faq: false,
                            login: false,
                            download: false,
                            about: false
                        })}><i className="fas fa-camera-retro"/> Prntscrn</p>
                    </div>
                    <div className={"right"}>
                        <div className={"sideBySide"}>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: false,
                                techSpecs: false,
                                faq: false,
                                login: true,
                                download: false,
                                about: false
                            })}><i className="fas fa-sign-in-alt"/> Login</p>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: false,
                                techSpecs: false,
                                faq: false,
                                login: false,
                                download: true,
                                about: false
                            })}><i className="fas fa-cloud-download-alt"/> Download</p>
                            <p onClick={() => this.setState({
                                default: false,
                                developers: false,
                                techSpecs: false,
                                faq: false,
                                about: true,
                                login: false,
                                download: false
                            })}><i className="fas fa-at"/> About</p>
                        </div>
                    </div>
                </div>
                {this.state.default ? <Default/> : null}
                {this.state.developers ? <Developers/> : null}
                {this.state.techSpecs ? <TechSpecs/> : null}
                {this.state.faq ? <Faq/> : null}
                {this.state.login ? <Login/> : null}
                {this.state.download ? <Download/> : null}
                {this.state.about ? <About/> : null}
            </>
        );
    }
}

class Default extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"wrapper-center"}>
                    <div className={"card"}>
                        <div className={"title"}>An Easy Way to Take Customizable Desktop Screenshots!</div>
                        <div className={"cardContainer"} style={{height: "90%"}}>
                            <div className={"button"}>
                                <p className={"sub-title"}>Login in or create an account to start</p>
                                <br/>
                                <a className={"cta"}>
                                    <span>Login</span>
                                    <svg width="13px" height="10px" viewBox="0 0 13 10">
                                        <path d="M1,5 L11,5"/>
                                        <polyline points="8 1 12 5 8 9"/>
                                    </svg>
                                </a>
                                <a className={"cta"}>
                                    <span>Create Account</span>
                                    <svg width="13px" height="10px" viewBox="0 0 13 10">
                                        <path d="M1,5 L11,5"/>
                                        <polyline points="8 1 12 5 8 9"/>
                                    </svg>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Developers extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"devL"}>
                    <div className={"wrapper-center"}>
                        <div className={"card"}>
                            <div className={"cardTop"}>
                                <div className={"cardTopLeft"}>
                                    <a className={"link"} href={"https://thiessem.ca"} target="_blank">
                                        <i className="fas fa-link"/></a>
                                </div>
                                <div className={"cardTopRight"}>
                                    <a className={"link"} href={"https://github.com/ideaeclipse"} target="_blank">
                                        <i className="icon fab fa-github"/></a>
                                </div>
                            </div>
                            <div className={"cardContainer-Top"}>
                                <div className={"name"}>Myles Thiessen</div>
                                <div className={"description"}>Api & Website</div>
                                <a className={"email"} href={"mailto:myles@prntscrn.ca"}>
                                    <i className="fas fa-envelope"/> myles@prntscrn.ca</a>
                                <p className={"heading"}>About</p>
                                <ul className={"list"}>
                                    <li>Student at the University of Toronto for Computer Science and Mathematics</li>
                                    <li>Known programming languages: Java, Ruby, (C/CPP), Python, (Node
                                        Js/React/HTML/CSS)
                                    </li>
                                    <li>I Use Arch BTW</li>
                                </ul>
                                <p className={"heading"}>Tasks</p>
                                <ul className={"list"}>
                                    <li><input type={"checkbox"} checked/> User Authentication System</li>
                                    <li><input type={"checkbox"} checked/> Basic Admin Panel</li>
                                    <li><input type={"checkbox"} checked/> Allow rails to work with MySQL instead of
                                        SqlLite
                                    </li>
                                    <li><input type={"checkbox"} checked/> Manage Uploads Via Admin Panel</li>
                                    <li><input type={"checkbox"} checked/> Setup Sites to run with Kubernetes</li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Create Public Website</li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Update Endpoints to allow for
                                        self Account Creation
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Update Images for Linking to a
                                        User Account
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Add functionality for Users to
                                        view all their uploaded images through the main site
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Setup a download api endpoint
                                        for the installer
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Create an faq section</li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Create a blog type page for
                                        developer logs
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div className={"devR"}>
                    <div className={"wrapper-center"}>
                        <div className={"card"}>
                            <div className={"cardTop"}>
                                <div className={"cardTopLeft"}>
                                    <a className={"link"} href={"https://google.ca"} target="_blank"><i
                                        className="fas fa-link"/></a>
                                </div>
                                <div className={"cardTopRight"}>
                                    <a className={"link"} href={"https://github.com/MingHaoC"} target="_blank"> <i
                                        className="icon fab fa-github"/></a>
                                </div>
                            </div>
                            <div className={"cardContainer-Top"}>
                                <div className={"name"}>Ming Chen</div>
                                <div className={"description"}>Desktop Client</div>
                                <a className={"email"} href={"mailto:ming@prntscrn.ca"}><i
                                    className="fas fa-envelope"/> ming@prntscrn.ca</a>
                                <p className={"heading"}>About</p>
                                <ul className={"list"}>
                                    <li>Student at the University of Windsor for Computer Science and Mathematics</li>
                                    <li>Totally isn't addicted to Tera (3,102 Hours Played)</li>
                                    <li>English Major at heart</li>
                                </ul>
                                <p className={"heading"}>Tasks</p>
                                <ul className={"list"}>
                                    <li><input type={"checkbox"} checked/> Take a screenshot of main monitor and
                                        save it
                                        as a png
                                    </li>
                                    <li><input type={"checkbox"} checked/> Display the saved image to the screen as
                                        an
                                        overlay
                                    </li>
                                    <li><input type={"checkbox"} checked/> Create a network utility to utilize the
                                        api
                                    </li>
                                    <li><input type={"checkbox"} checked/> Once the designated region is selected
                                        save
                                        and upload the image to the api
                                    </li>
                                    <li><input type={"checkbox"} checked/> Add multi monitor support</li>
                                    <li><input type={"checkbox"} checked/> Create installer to allow for downloads
                                        of a
                                        specific version
                                    </li>
                                    <li><input type={"checkbox"} checked/> Make a debug screen to log all relevant
                                        events
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Make the app a trayable app
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Add settings menu</li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Add support to view all
                                        uploaded images in client
                                    </li>
                                    <li><input type={"checkbox"} disabled={"disabled"}/> Support for drawing on the
                                        freezed image
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class TechSpecs extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"wrapper-center"}>
                    <div className={"card"}>
                        <div className={"cardContainer"}>
                            <div className={"center-with-width100"}>
                                <div className={"devL"}>
                                    <div className={"center-with-width80"}>
                                        <div className={"sub-title"}>Hosting</div>
                                        <div className={"list-title"}>DNS</div>
                                        <ul className={"list"}>
                                            <li>Cloudflare</li>
                                        </ul>
                                        <div className={"list-title"}>External Hosting</div>
                                        <ul className={"list"}>
                                            <li>GCP Compute Base Compute Unit (1 CU / 2 GB ram)</li>
                                            <br/>
                                            <li>External Nginx Proxy to forward all traffic</li>
                                            <br/>
                                            <li>GCP Storage Bucket (Coldline)</li>
                                        </ul>
                                        <div className={"list-title"}>Internal Hosting</div>
                                        <ul className={"list"}>
                                            <li>Custom built server (28 CU / 32 GB ram) running ESXI 6.7</li>
                                            <br/>
                                            <li>VM 1: (1 CU / 2 GB ram) Nginx reverse proxy</li>
                                            <br/>
                                            <li>VM 2: (2 CU / 4 GB ram) Postfix / Dovecot Host</li>
                                            <br/>
                                            <li>VM 3: (2 CU / 4 GB ram) MySQL Host</li>
                                            <br/>
                                            <li>VM 4: (2 CU / 4 GB ram) Kubernetes Master Node</li>
                                            <br/>
                                            <li>VM 5: (8 CU / 8 GB ram) Kubernetes Worker Node 1</li>
                                            <br/>
                                            <li>VM 6: (8 CU / 8 GB ram) Kubernetes Worker Node 2</li>
                                        </ul>
                                    </div>
                                </div>
                                <div className={"devR"}>
                                    <div className={"center-with-width80"}>
                                        <div className={"sub-title"}>Tech Stack</div>
                                        <div className={"list-title"}>Website</div>
                                        <ul className={"list"}>
                                            <li>Written using the React frontend framework</li>
                                            <br/>
                                            <li>Accessible at <a className={"hyper-link"}
                                                                 href={"https://prntscrn.ca"}>prntscrn.ca</a></li>
                                        </ul>
                                        <div className={"list-title"}>Admin Website</div>
                                        <ul className={"list"}>
                                            <li>Written using the React frontend framework</li>
                                            <br/>
                                            <li>Not publicly accessible</li>
                                        </ul>
                                        <div className={"list-title"}>API</div>
                                        <ul className={"list"}>
                                            <li>Written using Ruby on Rails</li>
                                            <br/>
                                            <li>MySQL for Rails Model data storage</li>
                                            <br/>
                                            <li>GCP Storage bucket for file storage</li>
                                        </ul>
                                        <div className={"list-title"}>Desktop Client</div>
                                        <ul className={"list"}>
                                            <li>Written in Java</li>
                                            <br/>
                                            <li>Uses JNativeHook for Keyboard and Mouse Listening</li>
                                            <br/>
                                            <li>Uses Java AWT for image capturing</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Faq extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"wrapper-center"}>
                    <div className={"card"}>
                        <div className={"cardContainer-Top"}>
                            <div className={"center-with-width50"}>
                                <div className={"title"}>FAQ</div>
                                <ul className={"list-noBullets"}>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                    <br/>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                    <br/>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                    <br/>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                    <br/>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                    <br/>
                                    <li>
                                        <text className={"question"}>Q: TEST</text>
                                        <br/>
                                        <br/>
                                        <text>A: TEST1</text>
                                    </li>
                                </ul>
                                <br/>
                                <div className={"footer"}>
                                    <text>If you have any questions that aren't answered here feel free to email either
                                        myles@prntscrn.ca or ming@prntscrn.ca
                                    </text>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Login extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"center"}>

                </div>
            </div>
        );
    }
}

class Download extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"center"}>
                    <p>Download</p>
                </div>
            </div>
        );
    }
}

class About extends Component {
    render() {
        return (
            <div className={"mainDiv"}>
                <div className={"wrapper-center"}>
                    <div className={"card"}>
                        <div className={"cardContainer"}>
                            <div className={"center-with-width50"}>
                                <div className={"title"}>About</div>
                                <ul className={"list"}>
                                    <li>We are two University Students who made this program with the intent that WE
                                        would use it
                                    </li>
                                    <br/>
                                    <li>Neither of us have programmed any website with intent for public use. This was a
                                        learning experience
                                    </li>
                                    <br/>
                                    <li>When designing this program we felt that giving users raw images would allow for
                                        more use cases that wouldn't be possible if your image was embedded in an html
                                        page
                                    </li>
                                    <br/>
                                    <li>Tried to write the majority of features with as little assistance from external
                                        libraries as possible
                                    </li>
                                    <br/>
                                    <li>We thought that if we were going to build a large scale backend for our multi
                                        services to interact with, we should allow anyone to interact with the api!
                                    </li>
                                    <br/>
                                    <li>We are always looking for more developers to help use with our various projects!
                                        Feel free to email either one of us if you are looking to talk with us or have
                                        ideas you'd like to share
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}