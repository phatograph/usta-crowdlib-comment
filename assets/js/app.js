var ItemBox = React.createClass({
  loadItemsFromServer: function() {
    $.ajax({
      url: this.props.url,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  handleItemSubmit: function(item) {
    $.ajax({
      url: "http://localhost:9998/items",
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      data: JSON.stringify(item),
      success: function(data) {
        this.setState({data: this.state.data.concat([data])});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState: function() {
    return {data: []};
  },
  componentDidMount: function() {
    this.loadItemsFromServer();
    // setInterval(this.loadItemsFromServer, this.props.pollInterval);
  },
  render: function() {
    return (
        <div className="itemBox">
        <h1>Items</h1>
        <ItemList data={this.state.data} />
        <ItemForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

var ItemList = React.createClass({
  render: function() {
    var itemNodes = this.props.data.map(function(item) {
      return (
          <Item title={item.title} id={item.id} key={item.id}>
          {`${item.user.name} ${item.user.surname}`}
          </Item>
          );
    });
    return (
        <div className="itemList">
        {itemNodes}
        </div>
        );
  }
});

var Item = React.createClass({
  render: function() {
    let link = `#/items/${this.props.id}`;
    return (
        <div className="item">
        <a href={link}><h2 className="">{this.props.title}</h2></a>
        {this.props.children}
        </div>
        );
  }
});

var ItemForm = React.createClass({
  getInitialState: function() {
    return {content: ''};
  },
  handleTitleChange: function(e) {
    this.setState({title: e.target.value});
  },
  handleSubmit: function(e) {
    e.preventDefault();
    var title = this.state.title.trim();
    if (!title) {
      return;
    }
    this.props.onItemSubmit({title: title});
    this.setState({title: ''});
  },
  render: function() {
    return (
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Title"
        value={this.state.title}
        onChange={this.handleTitleChange}
        />
        <input type="submit" value="Post" />
        </form>
        );
  }
});

var ItemInfo = React.createClass({
  loadItemsFromServer: function() {
    // $.ajax({
    //   url: this.props.url,
    //   dataType: 'json',
    //   cache: false,
    //   success: function(data) {
    //     this.setState({data: data});
    //   }.bind(this),
    //   error: function(xhr, status, err) {
    //     console.error(this.props.url, status, err.toString());
    //   }.bind(this)
    // });
  },
  handleItemSubmit: function(item) {
    // $.ajax({
    //   url: "http://localhost:9998/items",
    //   dataType: 'json',
    //   contentType: 'application/json',
    //   type: 'POST',
    //   data: JSON.stringify(item),
    //   success: function(data) {
    //     this.setState({data: this.state.data.concat([data])});
    //   }.bind(this),
    //   error: function(xhr, status, err) {
    //     console.error(this.props.url, status, err.toString());
    //   }.bind(this)
    // });
  },
  getInitialState: function() {
    return {data: []};
  },
  componentDidMount: function() {
    // this.loadItemsFromServer();
    // setInterval(this.loadItemsFromServer, this.props.pollInterval);
  },
  render: function() {
    return (
        <div className="itemBox">
        <h2>Items</h2>
        </div>
        );
  }
});

const App = React.createClass({
  getInitialState() {
    return {
      route: window.location.hash.substr(1)
    }
  },

  componentDidMount() {
    window.addEventListener('hashchange', () => {
      this.setState({
        route: window.location.hash.substr(1)
      })
    })
  },

  render() {
    switch (this.state.route) {
      case '/items': return (<ItemInfo />)
      default: return (<ItemBox url="http://localhost:9998/items" pollInterval={200} />)
    }
  }
})

ReactDOM.render(<App />, document.getElementById('content'))
